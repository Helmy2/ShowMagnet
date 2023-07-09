package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.remote.api.TvApi
import com.example.showmagnet.di.CurrentFirebaseUser
import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.tv.Episode
import com.example.showmagnet.domain.model.tv.Tv
import com.example.showmagnet.domain.repository.TvRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TvRepositoryImpl @Inject constructor(
    private val api: TvApi,
    private val firestore: FirebaseFirestore,
    @CurrentFirebaseUser private val user: FirebaseUser?
) : TvRepository {
    override suspend fun getDetails(id: Int): Result<Tv> = try {
        val response = api.getDetails(id)
        val favorite = isFavoriteTv(response.id)
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

    override suspend fun getSeason(id: Int, seasonNumber: Int): Result<List<Episode>> = try {
        val response = api.getSeason(id, seasonNumber)
        val result = response
            .episodes?.filterNotNull()?.filter { it.voteAverage != 0.0 }?.map { it.toDomain() }


        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getCast(id: Int): Result<List<Cast>> = try {
        val response =
            api.getCast(id)

        val result = response
            .cast?.filterNotNull()?.filter { it.profilePath != null }?.map { it.toDomain() }


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
        val response = api.getImages(id)
        val result =
            (response.backdrops.orEmpty() + response.posters.orEmpty()).filterNotNull()
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
        val response = api.getRecommendations(id)
        val result = response
            .shows?.filterNotNull()?.filter { it.posterPath != null }?.map { it.toDomain() }


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
        val response = api.getPopularTv()

        val result = response.shows?.filterNotNull()?.map { it.toDomain(MediaType.TV) }

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
        private const val TV = "tv"
        private const val ID = "id"
    }

    private fun getTvReference(): CollectionReference {
        val userId = user?.uid ?: throw Exception("User not found")

        return firestore.collection(USERS).document(userId).collection(TV)
    }

    override suspend fun addTvToFavoriteList(id: Int): Result<Boolean> {
        return try {
            val reference = getTvReference()

            reference.add(hashMapOf(ID to id)).await()

            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getTvFavoriteList(): Result<List<Int>> {
        return try {
            val reference = getTvReference()

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

    override suspend fun deleteFromFavoriteTvList(id: Int): Result<Boolean> {
        return try {
            val reference = getTvReference()

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

    override suspend fun isFavoriteTv(id: Int): Result<Boolean> {
        return try {
            val reference = getTvReference()

            val personQuery = reference.whereEqualTo(ID, id).get().await()
            Result.success(personQuery.documentChanges.isNotEmpty())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

