package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.tv.Episode
import com.example.showmagnet.domain.model.tv.Tv
import kotlinx.coroutines.flow.Flow

interface TvRepository {
    suspend fun getDetails(id: Int): Result<Tv>
    suspend fun getCast(id: Int): Result<List<Cast>>
    suspend fun getImages(id: Int): Result<List<Image>>
    suspend fun getRecommendations(id: Int): Result<List<Show>>
    suspend fun getSeason(id: Int, seasonNumber: Int): Result<List<Episode>>
    suspend fun isFavoriteTv(id: Int): Result<Boolean>
    suspend fun deleteFromFavoriteTvList(id: Int): Result<Boolean>
    suspend fun getTvFavoriteList(): Result<List<Int>>
    suspend fun addTvToFavoriteList(id: Int): Result<Boolean>
    suspend fun getFavoriteTv(): Result<List<Show>>
    suspend fun discoverTv(parameters: Map<String, String>): Result<List<Show>>
    suspend fun search(query: String, page: Int): Result<List<Show>>
    fun getCategory(category: Category): Flow<Result<List<Show>>>
}