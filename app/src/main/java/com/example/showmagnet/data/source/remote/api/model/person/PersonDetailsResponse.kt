package com.example.showmagnet.data.source.remote.api.model.person

import com.google.gson.annotations.SerializedName

data class PersonDetailsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("birthday") val birthDay: String?,
    @SerializedName("deathday") val deathDay: String?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("popularity") val popularity: Float?,
    @SerializedName("profile_path") val profilePath: String?
)


