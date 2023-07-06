package com.example.showmagnet.domain.model.person

import com.example.showmagnet.domain.model.Image

data class PersonDetails(
    val name: String,
    val biography: String,
    val birthDay: String,
    val deathDay: Any,
    val gender: Int,
    val homepage: Any,
    val id: Int,
    val knownForDepartment: String,
    val placeOfBirth: String,
    val popularity: Double,
    val profilePath: Image
)
