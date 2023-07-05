package com.example.showmagnet.data.source.remote.model

import com.example.showmagnet.domain.model.Genre

data class GenreRemote(
    val id: Int,
    val name: String
)

fun GenreRemote.toGenre() = Genre(
    id = id,
    name = name
)