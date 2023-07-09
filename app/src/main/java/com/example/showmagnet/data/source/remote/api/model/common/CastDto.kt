package com.example.showmagnet.data.source.remote.api.model.common

import com.google.gson.annotations.SerializedName

data class CastDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("character") val character: String?
)

