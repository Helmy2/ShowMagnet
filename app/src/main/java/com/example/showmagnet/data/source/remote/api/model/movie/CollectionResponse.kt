package com.example.showmagnet.data.source.remote.api.model.movie

import com.example.showmagnet.data.source.remote.api.model.common.ShowDto
import com.google.gson.annotations.SerializedName

data class CollectionResponse(
    @SerializedName("parts") val shows: List<ShowDto?>?
)