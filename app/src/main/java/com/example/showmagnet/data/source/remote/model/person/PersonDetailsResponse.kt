package com.example.showmagnet.data.source.remote.model.person

import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.person.PersonDetails
import com.google.gson.annotations.SerializedName

data class PersonDetailsResponse(
    val name: String,
    val biography: String,
    @SerializedName("birthday")
    val birthDay: String,
    @SerializedName("deathday")
    val deathDay: String?,
    @SerializedName("gender")
    val gender: Int,
    val homepage: String?,
    val id: Int,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    @SerializedName("place_of_birth")
    val placeOfBirth: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("profile_path")
    val profilePath: String
)


fun PersonDetailsResponse.toPersonDetails() = PersonDetails(
    name = name,
    biography = biography,
    birthDay = birthDay,
    deathDay = deathDay ?: "",
    gender = gender,
    homepage = homepage ?: "",
    id = id,
    knownForDepartment = knownForDepartment,
    placeOfBirth = placeOfBirth,
    popularity = popularity,
    profilePath = Image(profilePath)
)