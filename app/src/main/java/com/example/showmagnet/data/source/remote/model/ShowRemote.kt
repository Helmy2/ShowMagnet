package com.example.showmagnet.data.source.remote.model

import com.example.showmagnet.common.Constants
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
    posterPath = Constants.IMAGE_URL_W500 + posterPath,
    type =
    MediaType.values().firstOrNull { it.value == mediaType }
        ?: MediaType.MOVIE
)
