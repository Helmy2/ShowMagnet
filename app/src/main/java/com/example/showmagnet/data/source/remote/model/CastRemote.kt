package com.example.showmagnet.data.source.remote.model

import com.example.showmagnet.domain.model.Cast
import com.example.showmagnet.domain.model.Image
import com.google.gson.annotations.SerializedName

data class CastRemote(
    val id: Int,
    val name: String = "",
    @SerializedName("profile_path")
    val profilePath: String? = "",
    val character: String = ""
)

fun CastRemote.toCast() = Cast(
    id = id,
    name = name,
    profilePath = Image(profilePath),
    character
)