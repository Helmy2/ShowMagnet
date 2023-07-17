package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.local.LocalManager
import com.example.showmagnet.data.source.remote.api.PersonApi
import com.example.showmagnet.data.source.remote.api.RemoteManager
import com.example.showmagnet.data.source.remote.database.RemoteUserDataSource
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.domain.repository.PersonRepository
import com.example.showmagnet.utils.repositoryFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localManager: LocalManager,
    private val remoteManger: RemoteManager,
    private val api: PersonApi,
) : PersonRepository {
    init {
        remoteUserDataSource.setReference(Types.PERSONS)
    }

    override suspend fun getPersonDetails(id: Int): Result<PersonDetails> = try {
        val response = api.getPersonDetails(id)
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

    override suspend fun getImages(id: Int): Result<List<Image>> = try {
        val response = api.getPersonImages(id)
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

    override suspend fun getMovieCredits(id: Int): Result<List<Show>> = try {
        val response = api.getMovieCredits(id)
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

    override suspend fun getTvCredits(id: Int): Result<List<Show>> = try {
        val response = api.getTvCredits(id)
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

    override fun getTrendingPeople(timeWindow: TimeWindow): Flow<Result<List<Person>>> =
        repositoryFlow(
            localFlow =
            localManager.getALlPeople().map { list ->
                list.map { it.toDomain() }
            },
            updateFun = {
                val result = remoteManger.getTrendingPeople(timeWindow)
                localManager.insertPeople(result)
            },
        )

    override suspend fun getFavoritePeople(): Result<List<Person>> = try {
        val favoriteList = getPersonFavoriteList().getOrThrow()
        val list = mutableListOf<Person>()

        favoriteList.forEach {
            val person = api.getPersonDetails(it)
            list.add(
                Person(
                    id = person.id,
                    name = person.name.orEmpty(),
                    profilePath = Image(person.profilePath),
                    timeWindow = TimeWindow.DAY
                )
            )
        }

        Result.success(list)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override suspend fun addPersonToFavoriteList(id: Int) = remoteUserDataSource.addToFavorite(id)

    override suspend fun getPersonFavoriteList() = remoteUserDataSource.getFavoriteList()

    override suspend fun deleteFromFavoritePersonsList(id: Int) =
        remoteUserDataSource.deleteFromFavorite(id)

    override suspend fun isFavoritePersons(id: Int) = remoteUserDataSource.isFavorite(id)
}
