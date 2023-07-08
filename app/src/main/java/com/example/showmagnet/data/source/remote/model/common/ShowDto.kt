package com.example.showmagnet.data.source.remote.model.common

import com.google.gson.annotations.SerializedName

data class ShowDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("vote_average") val voteAverage: Float?,
    @SerializedName("poster_path") val posterPath: String?,
)

