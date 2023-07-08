package com.example.showmagnet.domain.model.tv

import com.example.showmagnet.domain.model.common.Image

data class Episode(
    val airDate: String,
    val episodeNumber: Int,
    val name: String,
    val overview: String,
    val runtime: Int,
    val stillPath: Image,
    val voteAverage: Number
)