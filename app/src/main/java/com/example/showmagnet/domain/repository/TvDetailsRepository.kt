package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.Cast
import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.Season
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.Tv

interface TvDetailsRepository {
    suspend fun getDetails(id: Int): Result<Tv>

    suspend fun getCast(id: Int): Result<List<Cast>>

    suspend fun getImages(id: Int): Result<List<Image>>

    suspend fun getRecommendations(id: Int): Result<List<Show>>
    suspend fun getSeason(id: Int, seasonNumber: Int): Result<Season>
}