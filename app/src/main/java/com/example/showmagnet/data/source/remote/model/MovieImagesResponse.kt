package com.example.showmagnet.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class MovieImagesResponse(
    @SerializedName("backdrops")
    val images: List<ImageRemote>,
)

