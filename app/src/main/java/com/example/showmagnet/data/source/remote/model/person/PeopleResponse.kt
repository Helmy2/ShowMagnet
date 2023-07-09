package com.example.showmagnet.data.source.remote.model.person

import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("results") val results: List<PersonDto?>?
)
