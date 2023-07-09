package com.example.showmagnet.domain.model.person

import com.example.showmagnet.domain.model.common.Image

data class PersonDetails(
    val name: String,
    val favorite: Boolean,
    val biography: String,
    val birthDay: String,
    val deathDay: Any,
    val gender: Int,
    val homepage: Any,
    val id: Int,
    val knownForDepartment: String,
    val placeOfBirth: String,
    val popularity: Float,
    val profilePath: Image
)
