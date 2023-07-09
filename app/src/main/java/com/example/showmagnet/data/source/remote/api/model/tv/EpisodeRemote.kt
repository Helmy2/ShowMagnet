package com.example.showmagnet.data.source.remote.api.model.tv

import com.google.gson.annotations.SerializedName

data class EpisodeRemote(
    @SerializedName("air_date") val airDate: String?,
    @SerializedName("episode_number") val episodeNumber: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("still_path") val stillPath: String?,
    @SerializedName("vote_average") val voteAverage: Double?
)

