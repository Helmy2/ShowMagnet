package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.TimeWindow

interface HomeRepository {

    suspend fun getPopular(mediaType: MediaType): Result<List<Show>>
    suspend fun getNowPlaying(mediaType: MediaType): Result<List<Show>>
    suspend fun getAnimation(mediaType: MediaType): Result<List<Show>>
    suspend fun getTrending(timeWindow: TimeWindow): Result<List<Show>>
}