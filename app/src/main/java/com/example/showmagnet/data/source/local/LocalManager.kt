package com.example.showmagnet.data.source.local

import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.local.model.ShowType
import com.example.showmagnet.domain.model.common.MediaType

interface LocalManager {
    fun getPeople(type: String): List<PersonDb>
    suspend fun deletePeople(type: String)

    suspend fun insertPeople(people: List<PersonDb>)
    suspend fun insertShow(shows: List<ShowDb>)
    fun getShows(type: ShowType, mediaType: MediaType): List<ShowDb>
    suspend fun deleteShows(type: ShowType, mediaType: MediaType)
}