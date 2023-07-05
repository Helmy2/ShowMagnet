package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.remote.model.CollectionResponse
import com.example.showmagnet.data.source.remote.model.CreditsResponse
import com.example.showmagnet.data.source.remote.model.MovieImagesResponse
import com.example.showmagnet.data.source.remote.model.MovieResponse
import com.example.showmagnet.data.source.remote.model.ShowResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailsApi {
    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieResponse

    @GET("movie/{id}/credits")
    suspend fun getMovieCast(@Path("id") id: Int): CreditsResponse

    @GET("movie/{id}/recommendations")
    suspend fun getMovieRecommendations(@Path("id") id: Int): ShowResponse

    @GET("collection/{id}")
    suspend fun getMovieCollection(@Path("id") id: Int): CollectionResponse

    @GET("movie/{id}/images")
    suspend fun getMovieImages(@Path("id") id: Int): MovieImagesResponse
}