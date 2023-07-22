package com.example.showmagnet.data.source.local

import com.example.showmagnet.data.source.local.model.PeopleType
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.local.model.ShowType
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.TimeWindow
import kotlinx.coroutines.flow.Flow

interface LocalManager {
    fun getPeople(type: String):  Flow<List<PersonDb>>
    suspend fun deletePeople(type: String, timeWindow: String)

    suspend fun refreshPeople(remotePeople: List<PersonDb>, type: PeopleType, timeWindow: TimeWindow)
    suspend fun refreshShow(remoteShows: List<ShowDb>, type: ShowType, mediaType: MediaType)
    fun getShows(type: ShowType): Flow<List<ShowDb>>
    suspend fun deleteShows(type: ShowType, mediaType: MediaType)
}