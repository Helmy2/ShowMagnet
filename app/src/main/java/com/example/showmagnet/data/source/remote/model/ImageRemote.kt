package com.example.showmagnet.data.source.remote.model

import com.example.showmagnet.domain.model.Image
import com.google.gson.annotations.SerializedName

data class ImageRemote(
    @SerializedName("file_path")
    val filePath: String,
    @SerializedName("aspect_ratio")
    val aspectRatio: Float
)

fun ImageRemote.toImage() = Image(url = filePath, ratio = aspectRatio)