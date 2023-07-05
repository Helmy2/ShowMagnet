package com.example.showmagnet.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CollectionResponse(
    @SerializedName("parts")
    val shows: List<ShowRemote>?
)