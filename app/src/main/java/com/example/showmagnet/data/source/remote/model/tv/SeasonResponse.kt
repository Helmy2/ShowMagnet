package com.example.showmagnet.data.source.remote.model.tv

import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.Season
import com.google.gson.annotations.SerializedName

data class SeasonResponse(
    val id: Int,
    val name: String,
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val episodes: List<EpisodeRemote>?,
)

fun SeasonResponse.toSeason() = Season(
    id = id,
    name = name,
    airDate = airDate ?: "",
    posterPath = Image(posterPath),
    episodes = episodes?.map { it.toEpisode() } ?: emptyList()
)