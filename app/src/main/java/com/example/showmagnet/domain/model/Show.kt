package com.example.showmagnet.domain.model

data class Show(
    val id: Int,
    val title: String,
    val voteAverage: Double,
    val posterPath: String,
    val type: MediaType
)