package com.example.showmagnet.data.source.local

import com.example.showmagnet.data.source.local.dao.PersonDao
import com.example.showmagnet.data.source.local.dao.ShowDao
import com.example.showmagnet.data.source.local.model.PeopleType
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.local.model.ShowType
import com.example.showmagnet.di.IoDispatcher
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.TimeWindow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalManagerImpl @Inject constructor(
    private val personDao: PersonDao,
    private val showDao: ShowDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalManager {

    override fun getPeople(type: String): Flow<List<PersonDb>> = personDao.getPeople(type)

    override suspend fun deletePeople(type: String, timeWindow: String) =
        personDao.deleteAllPeople(type, timeWindow)

    override suspend fun refreshPeople(
        remotePeople: List<PersonDb>, type: PeopleType, timeWindow: TimeWindow
    ) = withContext(ioDispatcher) {
        val localPeople = getPeople(type.name).first().filter { it.timeWindow == timeWindow.name }

        val toDelete = buildList {
            for (person in localPeople) {
                if (remotePeople.any { it.id == person.id })
                    continue
                add(person)
            }
        }
        val toSave = buildList {
            for (person in remotePeople) {
                if (localPeople.any { it.id == person.id })
                    continue
                add(person)
            }
        }

        personDao.insertPerson(toSave)
        personDao.deletePerson(toDelete)
    }


    override suspend fun refreshShow(
        remoteShows: List<ShowDb>,
        type: ShowType,
        mediaType: MediaType
    ): Unit = withContext(ioDispatcher) {
        val localShows =
            showDao.getCategory(type.name).first().filter { it.mediaType == mediaType.name }

        val toDelete = buildList {
            for (show in localShows) {
                if (remoteShows.any { it.id == show.id })
                    continue
                add(show)
            }
        }
        val toSave = buildList {
            for (show in remoteShows) {
                if (localShows.any { it.id == show.id })
                    continue
                add(show)
            }
        }

        showDao.insertShow(toSave)
        showDao.deleteShow(toDelete)
    }

    override fun getShows(type: ShowType): Flow<List<ShowDb>> = showDao.getCategory(type.name)

    override suspend fun deleteShows(type: ShowType, mediaType: MediaType) {
        showDao.deleteCategory(type.name, mediaType.name)
    }
}

