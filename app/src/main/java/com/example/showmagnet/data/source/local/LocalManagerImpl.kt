package com.example.showmagnet.data.source.local

import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.di.IoDispatcher
import com.example.showmagnet.domain.model.common.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalManagerImpl @Inject constructor(
    private val personDao: PersonDao,
    private val showDao: ShowDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalManager {

    override fun getALlPeople(): Flow<List<PersonDb>> = personDao.getAllPeople()

    override suspend fun insertPeople(people: List<PersonDb>) = withContext(ioDispatcher) {
        personDao.insertPerson(people)
    }

    override suspend fun insertShow(shows: List<ShowDb>) = withContext(ioDispatcher) {
        showDao.insertShow(shows)
    }

    override fun getCategory(category: Category): Flow<List<ShowDb>> =
        showDao.getCategory(category).map { list -> list.flatMap { it.shows } }

}