package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.remote.api.model.common.CreditsResponse
import com.example.showmagnet.data.source.remote.api.model.common.ShowResponse
import com.example.showmagnet.data.source.remote.api.model.movie.CollectionResponse
import com.example.showmagnet.data.source.remote.api.model.movie.ImagesResponse
import com.example.showmagnet.data.source.remote.api.model.movie.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MovieApi {
    @GET(ApiConstants.Movie.DETAILS_ENDPOINT)
    suspend fun getMovieDetails(@Path(ApiParameters.ID) id: Int): MovieResponse

    @GET(ApiConstants.Movie.CREDITS_ENDPOINT)
    suspend fun getMovieCast(@Path(ApiParameters.ID) id: Int): CreditsResponse

    @GET(ApiConstants.Movie.RECOMMENDATIONS_ENDPOINT)
    suspend fun getMovieRecommendations(@Path(ApiParameters.ID) id: Int): ShowResponse

    @GET(ApiConstants.Movie.COLLECTION_ENDPOINT)
    suspend fun getMovieCollection(@Path(ApiParameters.ID) id: Int): CollectionResponse

    @GET(ApiConstants.Movie.IMAGES_ENDPOINT)
    suspend fun getMovieImages(@Path(ApiParameters.ID) id: Int): ImagesResponse

    @GET(ApiConstants.Movie.POPULAR_ENDPOINT)
    suspend fun getPopularMovies(): ShowResponse

    @GET(ApiConstants.Movie.UPCOMING_ENDPOINT)
    suspend fun getUpcomingMovie(): ShowResponse

    @GET(ApiConstants.Discover.MOVIE)
    suspend fun discoverMovie(
        @QueryMap parameters: Map<String, String>
    ): ShowResponse

    @GET(ApiConstants.Movie.SEARCH_ENDPOINT)
    suspend fun search(
        @Query(ApiParameters.QUERY) query: String,
        @Query(ApiParameters.PAGE) page: Int
    ): ShowResponse

    @GET(ApiConstants.Discover.MOVIE_ANIME)
    suspend fun getAnimationMovies(): ShowResponse

    @GET(ApiConstants.Movie.TRENDING_ENDPOINT)
    suspend fun getTrending(): ShowResponse
}