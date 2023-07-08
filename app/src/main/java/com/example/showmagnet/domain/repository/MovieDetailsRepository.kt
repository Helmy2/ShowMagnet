package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.movie.Movie
import com.example.showmagnet.domain.model.common.Show

interface MovieDetailsRepository {
    suspend fun getMovieDetails(id: Int): Result<Movie>

    suspend fun getCast(id: Int): Result<List<Cast>>

    suspend fun getCollection(id: Int): Result<List<Show>>

    suspend fun getImages(id: Int): Result<List<Image>>

    suspend fun getRecommendations(id: Int): Result<List<Show>>
}