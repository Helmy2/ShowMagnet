package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.remote.model.ShowResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ShowApi {
    @GET("trending/all/{timeWindow}")
    suspend fun getTrending(@Path("timeWindow") timeWindow: String): ShowResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(): ShowResponse

    @GET("tv/popular")
    suspend fun getPopularTv(): ShowResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovie(): ShowResponse

    @GET("discover/movie?with_genres=16")
    suspend fun getAnimationMovies(): ShowResponse

    @GET("discover/tv?with_genres=16")
    suspend fun getAnimationTv(): ShowResponse


}