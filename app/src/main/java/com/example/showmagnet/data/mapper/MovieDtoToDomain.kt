package com.example.showmagnet.data.mapper

import com.example.showmagnet.data.source.remote.model.movie.MovieResponse
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.movie.Movie

fun MovieResponse.toDomain() = Movie(
    id = id,
    title = title.orEmpty(),
    adult = adult ?: false,
    backdropPath = Image(backdropPath),
    posterPath = Image(posterPath),
    genres = genres.map { it.toDomain() },
    overview = overview.orEmpty(),
    releaseDate = releaseDate.orEmpty(),
    runtime = runtime ?: -1,
    status = status.orEmpty(),
    voteAverage = voteAverage ?: -1f,
    collectionName = belongsToCollection?.name,
    collectionId = belongsToCollection?.id,
)