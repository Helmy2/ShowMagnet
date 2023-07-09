package com.example.showmagnet.data.source.remote.model.person

import com.google.gson.annotations.SerializedName

class PersonDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?
)
