package com.example.showmagnet.data.source.remote.model.common

import com.google.gson.annotations.SerializedName

data class GenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?
)

