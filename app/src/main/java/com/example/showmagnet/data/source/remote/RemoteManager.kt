package com.example.showmagnet.data.source.remote

import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.TimeWindow

interface RemoteManager {
    suspend fun getMovieCategory(category: Category): List<ShowDb>
    suspend fun getTvCategory(category: Category): List<ShowDb>
    suspend fun getFavoritePeople(): List<PersonDb>
    suspend fun getTrendingPeople(timeWindow: TimeWindow): List<PersonDb>
    suspend fun addToFavorite(id: Int): Result<Boolean>
    suspend fun deleteFromFavorite(id: Int): Result<Boolean>
    suspend fun isFavorite(id: Int): Result<Boolean>
    fun setReference(type: Types)
    suspend fun getFavoriteMovies(): List<ShowDb>
    suspend fun getFavoriteTvs(): List<ShowDb>
}