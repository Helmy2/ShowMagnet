package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.tv.Episode
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.tv.Tv

interface TvDetailsRepository {
    suspend fun getDetails(id: Int): Result<Tv>

    suspend fun getCast(id: Int): Result<List<Cast>>

    suspend fun getImages(id: Int): Result<List<Image>>

    suspend fun getRecommendations(id: Int): Result<List<Show>>
    suspend fun getSeason(id: Int, seasonNumber: Int): Result<List<Episode>>
}