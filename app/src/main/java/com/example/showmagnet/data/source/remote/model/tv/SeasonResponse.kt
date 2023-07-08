package com.example.showmagnet.data.source.remote.model.tv

import com.google.gson.annotations.SerializedName

data class SeasonResponse(
    @SerializedName("episodes") val episodes: List<EpisodeRemote?>?,
)
