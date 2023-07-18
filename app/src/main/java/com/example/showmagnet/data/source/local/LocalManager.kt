package com.example.showmagnet.data.source.local

import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.MediaType

interface LocalManager {
    fun getALlPeople(): List<PersonDb>
    suspend fun insertPeople(people: List<PersonDb>)
    suspend fun insertShow(shows: List<ShowDb>)
    fun getCategory(category: Category, type: MediaType): List<ShowDb>
    suspend fun deleteCategory(category: Category, mediaType: MediaType)
    suspend fun deleteAllPeople()
}