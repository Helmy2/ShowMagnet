package com.example.showmagnet.domain.model.movie

import com.example.showmagnet.domain.model.common.Genre
import com.example.showmagnet.domain.model.common.Image

data class Movie(
    val id: Int,
    val title: String,
    val favorite: Boolean,
    val adult: Boolean,
    val backdropPath: Image,
    val posterPath: Image,
    val collectionName: String?,
    val collectionId: Int?,
    val genres: List<Genre>,
    val overview: String,
    val releaseDate: String,
    val runtime: Int,
    val status: String,
    val voteAverage: Float
)


