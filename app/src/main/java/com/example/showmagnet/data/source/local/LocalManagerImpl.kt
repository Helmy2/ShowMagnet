package com.example.showmagnet.data.source.local

import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.di.IoDispatcher
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalManagerImpl @Inject constructor(
    private val personDao: PersonDao,
    private val showDao: ShowDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalManager {

    override fun getALlPeople(): List<PersonDb> = personDao.getAllPeople()

    override suspend fun deleteAllPeople() = personDao.deleteAllPeople()
    override suspend fun insertPeople(people: List<PersonDb>) = withContext(ioDispatcher) {
        personDao.insertPerson(people)
    }

    override suspend fun insertShow(shows: List<ShowDb>) = withContext(ioDispatcher) {
        showDao.insertShow(shows)
    }

    override fun getCategory(category: Category, type: MediaType): List<ShowDb> =
        showDao.getCategory(category, type)

    override suspend fun deleteCategory(category: Category, mediaType: MediaType) {
        showDao.deleteCategory(category, mediaType)
    }
}