package com.example.showmagnet.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class ShowResponse(
    val page: String,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val shows: List<ShowRemote>
)