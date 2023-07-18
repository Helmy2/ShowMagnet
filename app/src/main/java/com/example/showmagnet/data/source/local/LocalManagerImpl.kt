package com.example.showmagnet.data.source.local

import com.example.showmagnet.data.source.local.dao.PersonDao
import com.example.showmagnet.data.source.local.dao.ShowDao
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.local.model.ShowType
import com.example.showmagnet.di.IoDispatcher
import com.example.showmagnet.domain.model.common.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalManagerImpl @Inject constructor(
    private val personDao: PersonDao,
    private val showDao: ShowDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalManager {

    override fun getPeople(type: String, timeWindow: String): Flow<List<PersonDb>> =
        personDao.getPeople(type, timeWindow)

    override suspend fun deletePeople(type: String, timeWindow: String) =
        personDao.deleteAllPeople(type, timeWindow)

    override suspend fun insertPeople(people: List<PersonDb>) = withContext(ioDispatcher) {
        personDao.insertPerson(people)
    }

    override suspend fun insertShow(shows: List<ShowDb>) = withContext(ioDispatcher) {
        showDao.insertShow(shows)
    }

    override fun getShows(type: ShowType, mediaType: MediaType): Flow<List<ShowDb>> =
        showDao.getCategory(type.name, mediaType.name)

    override suspend fun deleteShows(type: ShowType, mediaType: MediaType) {
        showDao.deleteCategory(type.name, mediaType.name)
    }
}