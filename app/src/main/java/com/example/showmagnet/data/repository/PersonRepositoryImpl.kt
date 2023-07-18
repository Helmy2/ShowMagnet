package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.local.LocalManager
import com.example.showmagnet.data.source.local.model.PeopleType
import com.example.showmagnet.data.source.remote.RemoteManager
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.di.IoDispatcher
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.domain.repository.PersonRepository
import com.example.showmagnet.utils.handleErrors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val localManager: LocalManager,
    private val remoteManger: RemoteManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PersonRepository {
    init {
        remoteManger.setReference(Types.PERSONS)
    }

    override suspend fun getPersonDetails(id: Int): Result<PersonDetails> =
        withContext(ioDispatcher) {
            try {
                val response = remoteManger.getPersonDetails(id)
                val favorite = isFavoritePersons(response.id)

                val result = if (favorite.isSuccess) {
                    response.toDomain(favorite = favorite.getOrThrow())
                } else {
                    response.toDomain(false)
                }

                Result.success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }

    override suspend fun getImages(id: Int): Result<List<Image>> = withContext(ioDispatcher) {
        try {
            val response = remoteManger.getPersonImages(id)
            val result = response.profiles?.filterNotNull()?.map { it.toDomain() }

            if (result == null) {
                Result.failure(Exception("Something went wrong"))
            } else {
                Result.success(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getMovieCredits(id: Int): Result<List<Show>> = withContext(ioDispatcher) {
        try {
            val response = remoteManger.getMovieCredits(id)
            val result = (response.cast.orEmpty() + response.crow.orEmpty()).filterNotNull()
                .map { it.toDomain(MediaType.MOVIE) }

            if (result.isEmpty()) {
                Result.failure(Exception("Something went wrong"))
            } else {
                Result.success(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getTvCredits(id: Int): Result<List<Show>> = withContext(ioDispatcher) {
        try {
            val response = remoteManger.getTvCredits(id)
            val result = (response.cast.orEmpty() + response.crow.orEmpty()).filterNotNull()
                .map { it.toDomain(MediaType.TV) }

            if (result.isEmpty()) {
                Result.failure(Exception("Something went wrong"))
            } else {
                Result.success(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override fun getTrendingPeople(timeWindow: TimeWindow): Flow<Result<List<Person>>> = flow {
        val localResult = localManager.getPeople(PeopleType.POPULAR_PEOPLE.name)
        emit(Result.success(localResult.map { it.toDomain() }))

        val result = remoteManger.getPeople(PeopleType.POPULAR_PEOPLE, timeWindow)
        emit(Result.success(result.map { it.toDomain() }))

        localManager.deletePeople(PeopleType.POPULAR_PEOPLE.name)
        localManager.insertPeople(result)
    }.flowOn(ioDispatcher).handleErrors()


    override suspend fun getFavorite(): Flow<Result<List<Person>>> = flow {
        val localResult = localManager.getPeople(PeopleType.FAVORITE_PEOPLE.name)
        emit(Result.success(localResult.map { it.toDomain() }))

        val remoteReutlt = remoteManger.getPeople(PeopleType.POPULAR_PEOPLE, TimeWindow.DAY)
        emit(Result.success(remoteReutlt.map { it.toDomain() }))

        localManager.deletePeople(PeopleType.FAVORITE_PEOPLE.name)
        localManager.insertPeople(remoteReutlt)
    }.flowOn(ioDispatcher).handleErrors()

    override suspend fun addPersonToFavoriteList(id: Int) = remoteManger.addToFavorite(id)

    override suspend fun deleteFromFavoritePersonsList(id: Int) =
        remoteManger.deleteFromFavorite(id)

    private suspend fun isFavoritePersons(id: Int) = remoteManger.isFavorite(id)
}
