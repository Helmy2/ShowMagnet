package com.example.showmagnet.data.source.remote.api.model.movie

import com.example.showmagnet.data.source.remote.api.model.common.ImageDto
import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("backdrops") val backdrops: List<ImageDto?>?,
    @SerializedName("posters") val posters: List<ImageDto?>?,
)

