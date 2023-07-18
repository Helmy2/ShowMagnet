package com.example.showmagnet.data.source.local

import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.local.model.ShowType
import com.example.showmagnet.domain.model.common.MediaType
import kotlinx.coroutines.flow.Flow

interface LocalManager {
    fun getPeople(type: String, timeWindow: String): Flow<List<PersonDb>>
    suspend fun deletePeople(type: String, timeWindow: String)

    suspend fun insertPeople(people: List<PersonDb>)
    suspend fun insertShow(shows: List<ShowDb>)
    fun getShows(type: ShowType, mediaType: MediaType): Flow<List<ShowDb>>
    suspend fun deleteShows(type: ShowType, mediaType: MediaType)
}