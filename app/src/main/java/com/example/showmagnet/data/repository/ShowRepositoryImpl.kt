package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.remote.api.ShowApi
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.repository.ShowRepository
import javax.inject.Inject

class ShowRepositoryImpl @Inject constructor(
    private val api: ShowApi,
) : ShowRepository {

    override suspend fun getAnimation(mediaType: MediaType): Result<List<Show>> = try {
        val response = when (mediaType) {
            MediaType.MOVIE -> api.getAnimationMovies()
            MediaType.TV -> api.getAnimationTv()
        }
        val result = response.shows?.filterNotNull()?.map { it.toDomain(mediaType) }

        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getTrending(timeWindow: TimeWindow): Result<List<Show>> = try {
        val response = api.getTrending(timeWindow.value)

        val result = response.shows?.filterNotNull()?.map { it.toDomain() }

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

