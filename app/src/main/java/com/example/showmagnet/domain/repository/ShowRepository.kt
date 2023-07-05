package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.TimeWindow

interface ShowRepository {

    suspend fun getPopular(mediaType: MediaType): Result<List<Show>>
    suspend fun getAnimation(mediaType: MediaType): Result<List<Show>>
    suspend fun getTrending(timeWindow: TimeWindow): Result<List<Show>>
    suspend fun getUpcoming(): Result<List<Show>>
}