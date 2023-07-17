package com.example.showmagnet.data.mapper

import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.remote.api.model.person.PersonDetailsResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonDto
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.domain.model.person.PersonDetails
import java.time.LocalDateTime

fun PersonDetailsResponse.toDomain(favorite: Boolean) = PersonDetails(
    id = id,
    favorite = favorite,
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

fun PersonDto.toDb(timeWindow: TimeWindow, addedAt: LocalDateTime) = PersonDb(
    id = id,
    name = name.orEmpty(),
    profilePath = profilePath ?: throw IllegalArgumentException(),
    timeWindowDb = timeWindow,
    addedAt = addedAt
)

fun PersonDb.toDomain() = Person(
    id = id,
    name = name,
    profilePath = Image(profilePath),
    timeWindow = timeWindowDb
)