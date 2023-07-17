package com.example.showmagnet.data.source.local

import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.domain.model.common.Category
import kotlinx.coroutines.flow.Flow

interface LocalManager {
    fun getALlPeople(): Flow<List<PersonDb>>
    suspend fun insertPeople(people: List<PersonDb>)
    suspend fun insertShow(shows: List<ShowDb>)
    fun getCategory(category: Category): Flow<List<ShowDb>>
}