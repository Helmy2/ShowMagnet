package com.example.showmagnet.data.source.remote.model.movie

import com.google.gson.annotations.SerializedName

data class BelongsToCollectionDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?
)