package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.local.model.PeopleType
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.TimeWindow

interface RemoteManager {
    suspend fun getPeople(type: PeopleType,timeWindow: TimeWindow): List<PersonDb>
    suspend fun getMovieCategory(category: Category): List<ShowDb>
    suspend fun getTvCategory(category: Category): List<ShowDb>
}