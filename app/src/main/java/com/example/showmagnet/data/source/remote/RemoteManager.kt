package com.example.showmagnet.data.source.remote

import com.example.showmagnet.data.source.local.model.PeopleType
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.remote.api.model.common.CreditsResponse
import com.example.showmagnet.data.source.remote.api.model.common.ShowResponse
import com.example.showmagnet.data.source.remote.api.model.movie.CollectionResponse
import com.example.showmagnet.data.source.remote.api.model.movie.ImagesResponse
import com.example.showmagnet.data.source.remote.api.model.movie.MovieResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonCreditResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonDetailsResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonImagesResponse
import com.example.showmagnet.data.source.remote.api.model.tv.SeasonResponse
import com.example.showmagnet.data.source.remote.api.model.tv.TvResponse
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.TimeWindow

interface RemoteManager {
    suspend fun getPeople(type: PeopleType, timeWindow: TimeWindow): List<PersonDb>
    suspend fun getMovieCategory(category: Category): List<ShowDb>
    suspend fun getTvCategory(category: Category): List<ShowDb>
    suspend fun getPersonDetails(id: Int): PersonDetailsResponse
    suspend fun getPersonImages(id: Int): PersonImagesResponse
    suspend fun getMovieCredits(id: Int): PersonCreditResponse
    suspend fun getTvCredits(id: Int): PersonCreditResponse

    suspend fun addToFavorite(id: Int): Result<Boolean>

    suspend fun deleteFromFavorite(id: Int): Result<Boolean>

    suspend fun getFavoriteList(): Result<List<Int>>

    suspend fun isFavorite(id: Int): Result<Boolean>

    fun setReference(type: Types)

    suspend fun getTvDetails(id: Int): TvResponse
    suspend fun getTvSeason(id: Int, seasonNumber: Int): SeasonResponse
    suspend fun getTvCast(id: Int): CreditsResponse
    suspend fun getTvImages(id: Int): ImagesResponse
    suspend fun getTvRecommendations(id: Int): ShowResponse
    suspend fun searchTv(query: String, page: Int): ShowResponse
    suspend fun discoverTv(parameters: Map<String, String>): ShowResponse


    suspend fun getMovieDetails(id: Int): MovieResponse
    suspend fun getMovieCast(id: Int): CreditsResponse
    suspend fun getMovieCollection(id: Int): CollectionResponse
    suspend fun getMovieImages(id: Int): ImagesResponse
    suspend fun getMovieRecommendations(id: Int): ShowResponse
    suspend fun discoverMovie(parameters: Map<String, String>): ShowResponse
    suspend fun searchMovie(query: String, page: Int): ShowResponse

}