package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.remote.model.CreditsResponse
import com.example.showmagnet.data.source.remote.model.movie.ImagesResponse
import com.example.showmagnet.data.source.remote.model.show.ShowResponse
import com.example.showmagnet.data.source.remote.model.tv.SeasonResponse
import com.example.showmagnet.data.source.remote.model.tv.TvResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TvApi {
    @GET("tv/{id}")
    suspend fun getDetails(@Path("id") id: Int): TvResponse

    @GET("tv/{id}/credits")
    suspend fun getCast(@Path("id") id: Int): CreditsResponse

    @GET("tv/{id}/recommendations")
    suspend fun getRecommendations(@Path("id") id: Int): ShowResponse

    @GET("tv/{id}/images")
    suspend fun getImages(@Path("id") id: Int): ImagesResponse

    @GET("tv/{id}/season/{season_number}")
    suspend fun getSeason(
        @Path("id") id: Int,
        @Path("season_number") seasonNumber: Int
    ): SeasonResponse
}