package com.example.showmagnet.data.source.remote.api.model.common

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("cast") val cast: List<CastDto?>?
)

