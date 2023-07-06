package com.example.showmagnet.domain.model

data class Episode(
    val airDate: String,
    val episodeNumber: Int,
    val name: String,
    val overview: String,
    val runtime: Int,
    val stillPath: Image,
    val voteAverage: Double
)