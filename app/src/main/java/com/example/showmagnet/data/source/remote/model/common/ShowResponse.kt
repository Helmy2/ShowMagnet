package com.example.showmagnet.data.source.remote.model.common

import com.google.gson.annotations.SerializedName

data class ShowResponse(
    @SerializedName("page") val page: String?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("results") val shows: List<ShowDto?>?
)