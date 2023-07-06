package com.example.showmagnet.domain.model

data class Movie(
    val id: Int,
    val title: String,
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
    val voteAverage: Double
)


