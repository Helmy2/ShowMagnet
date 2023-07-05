package com.example.showmagnet.domain.model

data class Cast(
    val id: Int,
    val name: String = "",
    val profilePath: String? = "",
    val character: String = ""
)