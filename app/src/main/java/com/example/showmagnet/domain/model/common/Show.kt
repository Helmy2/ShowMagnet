package com.example.showmagnet.domain.model.common

data class Show(
    val id: Int,
    val title: String,
    val voteAverage: Float,
    val posterPath: Image,
    val mediaType: MediaType
)