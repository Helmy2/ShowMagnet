package com.example.showmagnet.domain.model

data class Season(
    val id: Int,
    val name: String,
    val airDate: String,
    val posterPath: Image,
    val episodes: List<Episode>,
)