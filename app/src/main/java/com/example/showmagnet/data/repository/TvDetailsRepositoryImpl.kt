package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.remote.api.TvApi
import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.tv.Episode
import com.example.showmagnet.domain.model.tv.Tv
import com.example.showmagnet.domain.repository.TvDetailsRepository
import javax.inject.Inject

class TvDetailsRepositoryImpl @Inject constructor(
    private val api: TvApi,
) : TvDetailsRepository {
    override suspend fun getDetails(id: Int): Result<Tv> = try {
        val response = api.getDetails(id).toDomain()
        Result.success(response)
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
}

