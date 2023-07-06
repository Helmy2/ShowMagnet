package com.example.showmagnet.data.source.remote.model.tv

import com.example.showmagnet.domain.model.Episode
import com.example.showmagnet.domain.model.Image
import com.google.gson.annotations.SerializedName

data class EpisodeRemote(
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episode_number")
    val episodeNumber: Int,
    val name: String,
    val overview: String?,
    val runtime: Int,
    @SerializedName("still_path")
    val stillPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double
)

fun EpisodeRemote.toEpisode() = Episode(
    airDate = airDate,
    episodeNumber = episodeNumber,
    name = name,
    overview = overview ?: "",
    runtime = runtime,
    stillPath = Image(stillPath),
    voteAverage = voteAverage
)