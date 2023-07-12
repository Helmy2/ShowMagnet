package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.movie.Movie

interface MovieRepository {
    suspend fun getMovieDetails(id: Int): Result<Movie>

    suspend fun getCast(id: Int): Result<List<Cast>>

    suspend fun getCollection(id: Int): Result<List<Show>>

    suspend fun getImages(id: Int): Result<List<Image>>

    suspend fun getRecommendations(id: Int): Result<List<Show>>
    suspend fun getUpcoming(): Result<List<Show>>
    suspend fun getPopular(): Result<List<Show>>
    suspend fun addMovieToFavoriteList(id: Int): Result<Boolean>
    suspend fun getMoviesFavoriteList(): Result<List<Int>>
    suspend fun deleteFromFavoriteMovieList(id: Int): Result<Boolean>
    suspend fun isFavoriteMovie(id: Int): Result<Boolean>
    suspend fun getFavoriteMovies(): Result<List<Show>>
    suspend fun discoverMovie(parameters: Map<String, String>): Result<List<Show>>
}