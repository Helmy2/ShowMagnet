package com.example.showmagnet.data.source.remote.model.show

import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.Show
import com.google.gson.annotations.SerializedName

data class ShowRemote(
    val id: Int,
    val title: String?,
    val name: String?,
    @SerializedName("media_type")
    val mediaType: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
)

fun ShowRemote.toShow() = Show(
    id = id,
    title = if (mediaType == "movie") title ?: "" else name
        ?: "",
    voteAverage = voteAverage,
    posterPath = Image(posterPath),
    type =
    MediaType.values().firstOrNull { it.value == mediaType }
        ?: MediaType.MOVIE
)

fun ShowRemote.toShow(type: MediaType) = Show(
    id = id,
    title = if (type == MediaType.MOVIE) title ?: "" else name
        ?: "",
    voteAverage = voteAverage,
    posterPath = Image(posterPath),
    type = type
)