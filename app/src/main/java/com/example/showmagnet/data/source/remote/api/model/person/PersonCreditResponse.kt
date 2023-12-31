package com.example.showmagnet.data.source.remote.api.model.person

import com.example.showmagnet.data.source.remote.api.model.common.ShowDto
import com.google.gson.annotations.SerializedName

data class PersonCreditResponse(
    @SerializedName("cast") val cast: List<ShowDto?>?,
    @SerializedName("crow") val crow: List<ShowDto?>?
)