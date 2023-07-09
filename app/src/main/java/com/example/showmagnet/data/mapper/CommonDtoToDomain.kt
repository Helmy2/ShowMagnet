package com.example.showmagnet.data.mapper

import com.example.showmagnet.data.source.remote.api.model.common.CastDto
import com.example.showmagnet.data.source.remote.api.model.common.GenreDto
import com.example.showmagnet.data.source.remote.api.model.common.ImageDto
import com.example.showmagnet.data.source.remote.api.model.common.ShowDto
import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Genre
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show


fun CastDto.toDomain() = Cast(
    id = id,
    name = name.orEmpty(),
    profilePath = Image(profilePath),
    character = character.orEmpty()
)

fun GenreDto.toDomain() = Genre(
    id = id,
    name = name.orEmpty()
)

fun ImageDto.toDomain() = Image(
    url = filePath,
    ratio = aspectRatio ?: -1f
)

fun ShowDto.toDomain() = Show(
    id = id,
    title = if (mediaType == "movie") title.orEmpty() else name.orEmpty(),
    voteAverage = voteAverage ?: -1f,
    posterPath = Image(posterPath),
    type = MediaType.values().firstOrNull { it.value == mediaType }
        ?: MediaType.MOVIE
)

fun ShowDto.toDomain(type: MediaType) = Show(
    id = id,
    title = if (type == MediaType.MOVIE) title.orEmpty() else name.orEmpty(),
    voteAverage = voteAverage ?: -1f,
    posterPath = Image(posterPath),
    type = type
)