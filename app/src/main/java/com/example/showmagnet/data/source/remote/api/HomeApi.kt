package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.remote.model.ShowResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi {
    @GET("trending/all/{timeWindow}")
    suspend fun getTrending(@Path("timeWindow") timeWindow: String): ShowResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(): ShowResponse

    @GET("tv/popular")
    suspend fun getPopularTv(): ShowResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovie(): ShowResponse

    @GET("tv/on_the_air")
    suspend fun getNowPlayingTv(): ShowResponse

    @GET("discover/movie?with_genres=16")
    suspend fun getAnimationMovies(): ShowResponse


    @GET("discover/tv?with_genres=16")
    suspend fun getAnimationTv(): ShowResponse


}