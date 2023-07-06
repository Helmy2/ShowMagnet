package com.example.showmagnet.data.repository

import com.example.showmagnet.data.source.remote.api.TvApi
import com.example.showmagnet.data.source.remote.model.show.toShow
import com.example.showmagnet.data.source.remote.model.toCast
import com.example.showmagnet.data.source.remote.model.toImage
import com.example.showmagnet.data.source.remote.model.tv.toEpisode
import com.example.showmagnet.data.source.remote.model.tv.toTv
import com.example.showmagnet.domain.model.Cast
import com.example.showmagnet.domain.model.Episode
import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.Tv
import com.example.showmagnet.domain.repository.TvDetailsRepository
import javax.inject.Inject

class TvDetailsRepositoryImpl @Inject constructor(
    private val api: TvApi,
) : TvDetailsRepository {
    override suspend fun getDetails(id: Int): Result<Tv> = try {
        val response = api.getDetails(id).toTv()
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getSeason(id: Int, seasonNumber: Int): Result<List<Episode>> = try {
        val response = api.getSeason(id, seasonNumber).episodes.filter { it.voteAverage != 0.0 }
            .map { it.toEpisode() }
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getCast(id: Int): Result<List<Cast>> = try {
        val response =
            api.getCast(id).cast.filter { it.profilePath != null }.map { it.toCast() }
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override suspend fun getImages(id: Int): Result<List<Image>> = try {
        val response = api.getImages(id)
        val result = (response.backdrops + response.posters).map { it.toImage() }
        Result.success(result)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getRecommendations(id: Int): Result<List<Show>> = try {
        val response = api.getRecommendations(id).shows.filter { it.posterPath != null }
            .map { it.toShow() }
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}

