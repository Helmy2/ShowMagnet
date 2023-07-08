package com.example.showmagnet.data.source.remote.model.movie

import com.example.showmagnet.data.source.remote.model.common.ShowDto
import com.google.gson.annotations.SerializedName

data class CollectionResponse(
    @SerializedName("parts") val shows: List<ShowDto?>?
)