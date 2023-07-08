package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.remote.ApiConstants
import com.example.showmagnet.data.source.remote.ApiParameters
import com.example.showmagnet.data.source.remote.model.common.ShowResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ShowApi {
    @GET(ApiConstants.TRAILERS_ENDPOINT)
    suspend fun getTrending(@Path(ApiParameters.TIME_WINDOW) timeWindow: String): ShowResponse

    @GET(ApiConstants.Movie.POPULAR_ENDPOINT)
    suspend fun getPopularMovies(): ShowResponse

    @GET(ApiConstants.Tv.POPULAR_ENDPOINT)
    suspend fun getPopularTv(): ShowResponse

    @GET(ApiConstants.Movie.UPCOMING_ENDPOINT)
    suspend fun getUpcomingMovie(): ShowResponse

    @GET(ApiConstants.Discover.MOVIE_ANIME)
    suspend fun getAnimationMovies(): ShowResponse

    @GET(ApiConstants.Discover.TV_ANIME)
    suspend fun getAnimationTv(): ShowResponse

}