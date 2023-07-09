package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.remote.api.MovieApi
import com.example.showmagnet.di.CurrentFirebaseUser
import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.movie.Movie
import com.example.showmagnet.domain.repository.MovieRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val firestore: FirebaseFirestore,
    @CurrentFirebaseUser private val user: FirebaseUser?
) : MovieRepository {
    override suspend fun getMovieDetails(id: Int): Result<Movie> = try {
        val response = api.getMovieDetails(id)
        val favorite = isFavoriteMovie(response.id)
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

    override suspend fun getCast(id: Int): Result<List<Cast>> = try {
        val response =
            api.getMovieCast(id)
        val result = response.cast?.filterNotNull()?.filter { it.profilePath != null }
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

    override suspend fun getCollection(id: Int): Result<List<Show>> = try {
        val response =
            api.getMovieCollection(id)
        val result = response.shows?.filterNotNull()?.filter { it.posterPath != null }
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

    override suspend fun getImages(id: Int): Result<List<Image>> = try {
        val response = api.getMovieImages(id)
        val result = (response.backdrops.orEmpty() + response.posters.orEmpty())
            .filterNotNull()
            .map { it.toDomain() }

        if (result.isEmpty()) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getRecommendations(id: Int): Result<List<Show>> = try {
        val response = api.getMovieRecommendations(id)
        val result = response.shows?.filterNotNull()?.filter { it.posterPath != null }
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

    override suspend fun getUpcoming(): Result<List<Show>> = try {
        val response = api.getUpcomingMovie()
        val result = response.shows?.filterNotNull()?.map { it.toDomain(MediaType.MOVIE) }

        Result.success(result)

        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getPopular(): Result<List<Show>> = try {
        val response = api.getPopularMovies()

        val result = response.shows?.filterNotNull()?.map { it.toDomain(MediaType.MOVIE) }

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
        private const val MOVIES = "movies"
        private const val ID = "id"
    }

    private fun getMovieReference(): CollectionReference {
        val userId = user?.uid ?: throw Exception("User not found")

        return firestore.collection(USERS).document(userId).collection(MOVIES)
    }

    override suspend fun addMovieToFavoriteList(id: Int): Result<Boolean> {
        return try {
            val reference = getMovieReference()

            reference.add(hashMapOf(ID to id)).await()

            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getMoviesFavoriteList(): Result<List<Int>> {
        return try {
            val reference = getMovieReference()

            val querySnapshot = reference.get().await()

            val list: MutableList<Int> = mutableListOf()
            for (document in querySnapshot.documents) {
                val id = document[ID].toString().toInt()
                id.let { list.add(it) }
            }
            Result.success(list)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun deleteFromFavoriteMovieList(id: Int): Result<Boolean> {
        return try {
            val reference = getMovieReference()

            val personQuery = reference.whereEqualTo(ID, id).get().await()
            if (personQuery.documents.isNotEmpty()) {
                for (document in personQuery) {
                    reference.document(document.id).delete().await()
                }
            }
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun isFavoriteMovie(id: Int): Result<Boolean> {
        return try {
            val reference = getMovieReference()

            val personQuery = reference.whereEqualTo(ID, id).get().await()
            Result.success(personQuery.documentChanges.isNotEmpty())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

