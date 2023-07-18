package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.mapper.toDb
import com.example.showmagnet.data.source.local.model.PeopleType
import com.example.showmagnet.data.source.local.model.PeopleType.FAVORITE_PEOPLE
import com.example.showmagnet.data.source.local.model.PeopleType.POPULAR_PEOPLE
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.remote.database.RemoteUserDataSource
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.Category.ANIME
import com.example.showmagnet.domain.model.common.Category.POPULAR
import com.example.showmagnet.domain.model.common.Category.TRENDING
import com.example.showmagnet.domain.model.common.Category.UPCOMING
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.TimeWindow
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class RemoteManagerImpl @Inject constructor(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val personApi: PersonApi,
    private val tvApi: TvApi,
    private val movieApi: MovieApi,
) : RemoteManager {

    init {
        remoteUserDataSource.setReference(Types.PERSONS)
    }

    override suspend fun getPeople(
        type: PeopleType,
        timeWindow: TimeWindow
    ): List<PersonDb> {
        val now = LocalDateTime.now()
        val response = when (type) {
            POPULAR_PEOPLE -> personApi.getPopularPeople(timeWindow.value).results?.filterNotNull()
                ?.filter { it.profilePath != null }?.map {
                    it.toDb(type.name, timeWindow.name, now)
                } ?: emptyList()

            FAVORITE_PEOPLE -> getFavoritePeople()
        }
        return response
    }

    private suspend fun getFavoritePeople(): List<PersonDb> {
        val favoriteList = remoteUserDataSource.getFavoriteList().getOrThrow()

        val remoteReutlt = mutableListOf<PersonDb>()
        favoriteList.forEach {
            val person = personApi.getPersonDetails(it)
            remoteReutlt.add(
                PersonDb(
                    id = person.id,
                    name = person.name.orEmpty(),
                    type = FAVORITE_PEOPLE.name,
                    addedAt = LocalDateTime.now(),
                    profilePath = person.profilePath.orEmpty(),
                    timeWindow = TimeWindow.DAY.name
                )
            )
        }
        return remoteReutlt
    }

    override suspend fun getMovieCategory(category: Category): List<ShowDb> {
        val response = when (category) {
            UPCOMING -> movieApi.getUpcomingMovie()
            TRENDING -> movieApi.getTrending()
            POPULAR -> movieApi.getPopularMovies()
            ANIME -> movieApi.getAnimationMovies()
        }

        val result = response.shows?.filterNotNull()?.map {
            it.toDb(
                addedAt = LocalDateTime.now(),
                type = category.name,
                mediaType = MediaType.MOVIE,
            )
        } ?: emptyList()

        return result
    }

    override suspend fun getTvCategory(category: Category): List<ShowDb> {
        val response = when (category) {
            UPCOMING -> {
                val data = LocalDate.now()
                tvApi.getUpcoming(data.minusWeeks(1).toString(), data.plusWeeks(1).toString())
            }

            TRENDING -> tvApi.getTrending()
            POPULAR -> tvApi.getPopularTv()
            ANIME -> tvApi.getAnimationTv()
        }

        val result = response.shows?.filterNotNull()?.filter { it.posterPath != null }?.map {
            it.toDb(
                addedAt = LocalDateTime.now(),
                type = category.name,
                mediaType = MediaType.TV,
            )
        }.orEmpty()

        return result
    }
}