package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.Cast
import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.Movie
import com.example.showmagnet.domain.model.Show

interface MovieDetailsRepository {
    suspend fun getMovieDetails(id: Int): Result<Movie>

    suspend fun getCast(id: Int): Result<List<Cast>>

    suspend fun getCollection(id: Int): Result<List<Show>>

    suspend fun getImages(id: Int): Result<List<Image>>

    suspend fun getRecommendations(id: Int): Result<List<Show>>
}