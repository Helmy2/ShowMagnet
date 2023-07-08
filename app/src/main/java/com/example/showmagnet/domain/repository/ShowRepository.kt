package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow

interface ShowRepository {

    suspend fun getPopular(mediaType: MediaType): Result<List<Show>>
    suspend fun getAnimation(mediaType: MediaType): Result<List<Show>>
    suspend fun getTrending(timeWindow: TimeWindow): Result<List<Show>>
    suspend fun getUpcoming(): Result<List<Show>>
}