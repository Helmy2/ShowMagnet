package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.remote.api.PersonApi
import com.example.showmagnet.data.source.remote.database.RemoteDataSource
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.domain.repository.PersonRepository
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val api: PersonApi,
) : PersonRepository {
    init {
        remoteDataSource.setReference(Types.PERSONS)
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

    override suspend fun getTrendingPeople(timeWindow: TimeWindow): Result<List<Person>> = try {
        val response = api.getTrendingPeople(timeWindow.value)
        val result = response.results?.filterNotNull()?.filter { it.profilePath != null }
            ?.map { it.toDomain() }

        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getFavoritePeople(): Result<List<Person>> = try {
        val favoriteList = getPersonFavoriteList().getOrThrow()
        val list = mutableListOf<Person>()

        favoriteList.forEach {
            val person = api.getPersonDetails(it)
            list.add(
                Person(
                    id = person.id,
                    name = person.name.orEmpty(),
                    profilePath = Image(person.profilePath)
                )
            )
        }

        Result.success(list)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override suspend fun addPersonToFavoriteList(id: Int) =
        remoteDataSource.addToFavorite(id)

    override suspend fun getPersonFavoriteList() = remoteDataSource.getFavoriteList()

    override suspend fun deleteFromFavoritePersonsList(id: Int) =
        remoteDataSource.deleteFromFavorite(id)

    override suspend fun isFavoritePersons(id: Int) = remoteDataSource.isFavorite(id)
}
