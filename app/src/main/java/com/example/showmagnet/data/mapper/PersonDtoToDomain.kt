package com.example.showmagnet.data.mapper

import com.example.showmagnet.data.source.remote.model.person.PersonDetailsResponse
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.person.PersonDetails

fun PersonDetailsResponse.toDomain() = PersonDetails(
    id = id,
    name = name.orEmpty(),
    biography = biography.orEmpty(),
    birthDay = birthDay.orEmpty(),
    deathDay = deathDay.orEmpty(),
    gender = gender ?: -1,
    homepage = homepage.orEmpty(),
    knownForDepartment = knownForDepartment.orEmpty(),
    placeOfBirth = placeOfBirth.orEmpty(),
    popularity = popularity ?: -1f,
    profilePath = Image(profilePath)
)