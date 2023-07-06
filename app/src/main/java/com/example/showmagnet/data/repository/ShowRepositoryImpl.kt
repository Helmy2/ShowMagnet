package com.example.showmagnet.data.repository

import com.example.showmagnet.data.source.remote.api.ShowApi
import com.example.showmagnet.data.source.remote.model.show.toShow
import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.TimeWindow
import com.example.showmagnet.domain.repository.ShowRepository
import javax.inject.Inject

class ShowRepositoryImpl @Inject constructor(
    private val api: ShowApi,
) : ShowRepository {
    override suspend fun getPopular(mediaType: MediaType): Result<List<Show>> = try {
        val response = when (mediaType) {
            MediaType.MOVIE -> api.getPopularMovies()
            MediaType.TV -> api.getPopularTv()
        }
        Result.success(response.shows.map { it.toShow(mediaType) })
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getUpcoming(): Result<List<Show>> = try {
        val response = api.getUpcomingMovie()

        Result.success(response.shows.map { it.toShow(MediaType.MOVIE) })
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getAnimation(mediaType: MediaType): Result<List<Show>> = try {
        val response = when (mediaType) {
            MediaType.MOVIE -> api.getAnimationMovies()
            MediaType.TV -> api.getAnimationTv()
        }
        Result.success(response.shows.map { it.toShow(mediaType) })
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getTrending(timeWindow: TimeWindow): Result<List<Show>> = try {
        val response = api.getTrending(timeWindow.value)
        Result.success(response.shows.map { it.toShow() })
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


}

