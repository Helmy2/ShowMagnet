package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.remote.api.PersonApi
import com.example.showmagnet.data.source.remote.database.RemoteDataSource
import com.example.showmagnet.di.CurrentFirebaseUser
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.domain.repository.PersonRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val firestore: FirebaseFirestore,
    @CurrentFirebaseUser private val user: FirebaseUser?,
    private val api: PersonApi,
) : PersonRepository {
    private val userId = user?.uid ?: throw Exception("User not found")

    private val reference = firestore.collection(USERS).document(userId).collection(Person)

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

    companion object {
        private const val USERS = "users"
        private const val Person = "person"
    }


    override suspend fun addPersonToFavoriteList(id: Int) =
        remoteDataSource.addToFavorite(reference, id)

    override suspend fun getPersonFavoriteList() = remoteDataSource.getFavoriteList(reference)

    override suspend fun deleteFromFavoritePersonsList(id: Int) =
        remoteDataSource.deleteFromFavorite(reference, id)

    override suspend fun isFavoritePersons(id: Int) = remoteDataSource.isFavorite(reference, id)
}
