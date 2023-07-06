package com.example.showmagnet.domain.model

data class Tv(
    val id: Int,
    val name: String,
    val adult: Boolean?,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val backdropPath: Image,
    val posterPath: Image,
    val genres: List<Genre>,
    val overview: String?,
    val firstAirDate: String?,
    val voteAverage: Double?
)