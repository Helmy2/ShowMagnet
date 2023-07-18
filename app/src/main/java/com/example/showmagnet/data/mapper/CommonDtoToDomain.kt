package com.example.showmagnet.data.mapper

import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.local.model.ShowType
import com.example.showmagnet.data.source.remote.api.model.common.CastDto
import com.example.showmagnet.data.source.remote.api.model.common.GenreDto
import com.example.showmagnet.data.source.remote.api.model.common.ImageDto
import com.example.showmagnet.data.source.remote.api.model.common.ShowDto
import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.Genre
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import java.time.LocalDateTime


fun CastDto.toDomain() = Cast(
    id = id,
    name = name.orEmpty(),
    profilePath = Image(profilePath ?: "", .7f),
    character = character.orEmpty()
)

fun GenreDto.toDomain() = Genre(
    id = id,
    name = name.orEmpty()
)

fun ImageDto.toDomain() = Image(
    url = filePath ?: "",
    ratio = aspectRatio ?: -1f
)


fun ShowDto.toDomain() = Show(
    id = id,
    title = if (mediaType == "movie") title.orEmpty() else name.orEmpty(),
    voteAverage = voteAverage ?: -1f,
    posterPath = Image(posterPath ?: "", .7f),
    mediaType = MediaType.values().firstOrNull { it.value == mediaType }
        ?: MediaType.MOVIE
)

fun ShowDto.toDomain(type: MediaType) = Show(
    id = id,
    title = if (type == MediaType.MOVIE) title.orEmpty() else name.orEmpty(),
    voteAverage = voteAverage ?: -1f,
    posterPath = Image(posterPath ?: "", .7f),
    mediaType = type
)

fun ShowDto.toDb(
    addedAt: LocalDateTime,
    mediaType: MediaType,
    type: String,
) = ShowDb(
    id = id,
    title = if (mediaType == MediaType.MOVIE) title.orEmpty() else name.orEmpty(),
    voteAverage = voteAverage ?: -1f,
    posterPath = posterPath ?: "",
    mediaType = mediaType.name,
    type = type,
    addedAt = addedAt
)

fun ShowDb.toDomain() = Show(
    id = id,
    title = title,
    voteAverage = voteAverage,
    posterPath = Image(posterPath),
    mediaType = MediaType.valueOf(mediaType)
)

fun Category.toShowType() = when (this) {
    Category.UPCOMING -> ShowType.UPCOMING_SHOW
    Category.TRENDING -> ShowType.TRENDING_SHOW
    Category.POPULAR -> ShowType.POPULAR_SHOW
    Category.ANIME -> ShowType.ANIME_SHOW
}