package com.example.showmagnet.data.source.remote.model

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
