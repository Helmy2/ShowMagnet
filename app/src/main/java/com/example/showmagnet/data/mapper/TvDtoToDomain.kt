package com.example.showmagnet.data.mapper

import com.example.showmagnet.data.source.remote.model.tv.EpisodeRemote
import com.example.showmagnet.data.source.remote.model.tv.TvResponse
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.tv.Episode
import com.example.showmagnet.domain.model.tv.Tv

fun EpisodeRemote.toDomain() = Episode(
    airDate = airDate.orEmpty(),
    episodeNumber = episodeNumber ?: -1,
    name = name.orEmpty(),
    overview = overview.orEmpty(),
    runtime = runtime ?: -1,
    stillPath = Image(stillPath),
    voteAverage = voteAverage ?: -1f
)

fun TvResponse.toDomain() = Tv(
    id = id,
    name = name.orEmpty(),
    adult = adult ?: false,
    numberOfEpisodes = numberOfEpisodes ?: -1,
    numberOfSeasons = numberOfSeasons ?: -1,
    backdropPath = Image(backdropPath),
    posterPath = Image(posterPath),
    genres = genres?.filterNotNull()?.map { it.toDomain() }.orEmpty(),
    overview = overview.orEmpty(),
    firstAirDate = firstAirDate.orEmpty(),
    voteAverage = voteAverage ?: -1f
)