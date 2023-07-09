package com.example.showmagnet.domain.model.tv

import com.example.showmagnet.domain.model.common.Genre
import com.example.showmagnet.domain.model.common.Image

data class Tv(
    val id: Int,
    val name: String,
    val favorite: Boolean,
    val adult: Boolean?,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val backdropPath: Image,
    val posterPath: Image,
    val genres: List<Genre>,
    val overview: String?,
    val firstAirDate: String?,
    val voteAverage: Float
)