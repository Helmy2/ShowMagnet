package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.local.LocalManager
import com.example.showmagnet.data.source.local.model.PeopleType
import com.example.showmagnet.data.source.remote.RemoteManager
import com.example.showmagnet.data.source.remote.api.PersonApi
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.di.IoDispatcher
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.domain.repository.PersonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val localManager: LocalManager,
    private val remoteManger: RemoteManager,
    private val personApi: PersonApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PersonRepository {
    init {
        remoteManger.setReference(Types.PERSONS)
    }

    override suspend fun getPersonDetails(id: Int): Result<PersonDetails> =
        withContext(ioDispatcher) {
            try {
                val response = personApi.getPersonDetails(id)
                val favorite = remoteManger.isFavorite(response.id)

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
            val response = personApi.getPersonImages(id)
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
            val response = personApi.getMovieCredits(id)
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
            val response = personApi.getTvCredits(id)
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

    override fun getTrendingPeople(timeWindow: TimeWindow): Flow<List<Person>> =
        localManager.getPeople(PeopleType.POPULAR_PEOPLE.name)
            .distinctUntilChanged()
            .map { list -> list.map { it.toDomain() } }.flowOn(ioDispatcher)


    override suspend fun refreshTrending(timeWindow: TimeWindow) {
        val remoteReutlt = remoteManger.getTrendingPeople(timeWindow)

        localManager.refreshPeople(
            remoteReutlt,
            PeopleType.POPULAR_PEOPLE,
            timeWindow
        )
    }

    override suspend fun getFavorite(): Flow<List<Person>> =
        localManager.getPeople(PeopleType.FAVORITE_PEOPLE.name)
            .distinctUntilChanged()
            .map { list -> list.map { it.toDomain() } }.flowOn(ioDispatcher)

    override suspend fun refreshFavorite() {
        val remoteReutlt = remoteManger.getFavoritePeople()

        localManager.refreshPeople(remoteReutlt, PeopleType.FAVORITE_PEOPLE, TimeWindow.DAY)
    }

    override suspend fun addPersonToFavoriteList(id: Int) = remoteManger.addToFavorite(id)

    override suspend fun deleteFromFavoritePersonsList(id: Int) =
        remoteManger.deleteFromFavorite(id)

}
