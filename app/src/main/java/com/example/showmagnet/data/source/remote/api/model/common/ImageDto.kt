package com.example.showmagnet.data.source.remote.api.model.common

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("file_path") val filePath: String?,
    @SerializedName("aspect_ratio") val aspectRatio: Float?
)

