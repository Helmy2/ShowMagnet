package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.remote.api.model.common.CreditsResponse
import com.example.showmagnet.data.source.remote.api.model.common.ShowResponse
import com.example.showmagnet.data.source.remote.api.model.movie.ImagesResponse
import com.example.showmagnet.data.source.remote.api.model.tv.SeasonResponse
import com.example.showmagnet.data.source.remote.api.model.tv.TvResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TvApi {
    @GET(ApiConstants.Tv.DETAILS_ENDPOINT)
    suspend fun getDetails(@Path(ApiParameters.ID) id: Int): TvResponse

    @GET(ApiConstants.Tv.CREDITS_ENDPOINT)
    suspend fun getCast(@Path(ApiParameters.ID) id: Int): CreditsResponse

    @GET(ApiConstants.Tv.RECOMMENDATIONS_ENDPOINT)
    suspend fun getRecommendations(@Path(ApiParameters.ID) id: Int): ShowResponse

    @GET(ApiConstants.Tv.IMAGES_ENDPOINT)
    suspend fun getImages(@Path(ApiParameters.ID) id: Int): ImagesResponse

    @GET(ApiConstants.Tv.SEASON_ENDPOINT)
    suspend fun getSeason(
        @Path(ApiParameters.ID) id: Int,
        @Path(ApiParameters.SEASON_NUMBER) seasonNumber: Int
    ): SeasonResponse

    @GET(ApiConstants.Tv.POPULAR_ENDPOINT)
    suspend fun getPopularTv(): ShowResponse

    @GET(ApiConstants.Discover.TV_ANIME)
    suspend fun getAnimationTv(): ShowResponse

    @GET(ApiConstants.Discover.TV)
    suspend fun discoverTv(
        @QueryMap parameters: Map<String, String>
    ): ShowResponse

    @GET(ApiConstants.Tv.SEARCH_ENDPOINT)
    suspend fun search(
        @Query(ApiParameters.QUERY) query: String,
        @Query(ApiParameters.PAGE) page: Int
    ): ShowResponse

    @GET(ApiConstants.Tv.TRENDING_ENDPOINT)
    suspend fun getTrending(): ShowResponse

    @GET(ApiConstants.Discover.TV)
    suspend fun getUpcoming(
        @Query(ApiParameters.FIRST_AIR_DATE_START) start: String,
        @Query(ApiParameters.FIRST_AIR_DATE_END) end: String
    ): ShowResponse
}