package com.example.showmagnet.data.repository

import com.example.showmagnet.data.source.remote.api.HomeApi
import com.example.showmagnet.data.source.remote.model.toListShow
import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.TimeWindow
import com.example.showmagnet.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
) : HomeRepository {
    override suspend fun getPopular(mediaType: MediaType): Result<List<Show>> = try {
        val response = when (mediaType) {
            MediaType.MOVIE -> api.getPopularMovies()
            MediaType.TV -> api.getPopularTv()
        }
        Result.success(response.toListShow(mediaType))
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getUpcoming(): Result<List<Show>> = try {
        val response = api.getUpcomingMovie()

        Result.success(response.toListShow(MediaType.MOVIE))
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getAnimation(mediaType: MediaType): Result<List<Show>> = try {
        val response = when (mediaType) {
            MediaType.MOVIE -> api.getAnimationMovies()
            MediaType.TV -> api.getAnimationTv()
        }
        Result.success(response.toListShow(mediaType))
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getTrending(timeWindow: TimeWindow): Result<List<Show>> = try {
        val response = api.getTrending(timeWindow.value)
        Result.success(response.toListShow())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


}

