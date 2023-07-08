package com.example.showmagnet.data.source.remote.model.person

import com.example.showmagnet.data.source.remote.model.common.ImageDto
import com.google.gson.annotations.SerializedName

data class PersonImagesResponse(
    @SerializedName("profiles") val profiles: List<ImageDto?>?
)