package com.example.showmagnet.data.repository

import com.example.showmagnet.data.source.remote.api.MovieDetailsApi
import com.example.showmagnet.data.source.remote.model.toCast
import com.example.showmagnet.data.source.remote.model.toImage
import com.example.showmagnet.data.source.remote.model.toListShow
import com.example.showmagnet.data.source.remote.model.toMovie
import com.example.showmagnet.data.source.remote.model.toShow
import com.example.showmagnet.domain.model.Cast
import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.Movie
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val api: MovieDetailsApi,
) : MovieDetailsRepository {
    override suspend fun getMovieDetails(id: Int): Result<Movie> = try {
        val response = api.getMovieDetails(id).toMovie()
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getCast(id: Int): Result<List<Cast>> = try {
        val response = api.getMovieCast(id).cast.map { it.toCast() }
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getCollection(id: Int): Result<List<Show>> = try {
        val response =
            api.getMovieCollection(id).shows?.map { it.toShow() }

        if (response == null)
            Result.failure(Exception("Response is null"))
        else
            Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getImages(id: Int): Result<List<Image>> = try {
        val response = api.getMovieImages(id).images.map { it.toImage() }
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getRecommendations(id: Int): Result<List<Show>> = try {
        val response = api.getMovieRecommendations(id)
        Result.success(response.toListShow())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}

