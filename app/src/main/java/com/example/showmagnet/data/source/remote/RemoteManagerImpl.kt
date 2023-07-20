package com.example.showmagnet.data.source.remote

import com.example.showmagnet.data.mapper.toDb
import com.example.showmagnet.data.mapper.toShowType
import com.example.showmagnet.data.source.local.model.PeopleType.FAVORITE_PEOPLE
import com.example.showmagnet.data.source.local.model.PeopleType.POPULAR_PEOPLE
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.local.model.ShowType
import com.example.showmagnet.data.source.remote.api.MovieApi
import com.example.showmagnet.data.source.remote.api.PersonApi
import com.example.showmagnet.data.source.remote.api.TvApi
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

    override suspend fun getTrendingPeople(timeWindow: TimeWindow): List<PersonDb> {
        val now = LocalDateTime.now()
        val response = personApi.getPopularPeople(timeWindow.value).results?.filterNotNull()
            ?.filter { it.profilePath != null }?.map {
                it.toDb(POPULAR_PEOPLE.name, timeWindow.name, now)
            } ?: emptyList()

        return response
    }

    override suspend fun getFavoritePeople(): List<PersonDb> {
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

    override suspend fun getFavoriteMovies(): List<ShowDb> {
        val favoriteList = remoteUserDataSource.getFavoriteList().getOrThrow()

        val remoteReutlt = mutableListOf<ShowDb>()
        favoriteList.forEach {
            val movie = movieApi.getDetails(it)
            remoteReutlt.add(
                ShowDb(
                    id = movie.id,
                    title = movie.title.orEmpty(),
                    voteAverage = movie.voteAverage ?: 0f,
                    posterPath = movie.posterPath ?: "",
                    mediaType = MediaType.MOVIE.name,
                    type = ShowType.FAVORITE_SHOW.name,
                    addedAt = LocalDateTime.now()
                )
            )
        }

        return remoteReutlt
    }

    override suspend fun getFavoriteTvs(): List<ShowDb> {
        val favoriteList = remoteUserDataSource.getFavoriteList().getOrThrow()

        val remoteReutlt = mutableListOf<ShowDb>()
        favoriteList.forEach {
            val movie = tvApi.getDetails(it)
            remoteReutlt.add(
                ShowDb(
                    id = movie.id,
                    title = movie.name.orEmpty(),
                    voteAverage = movie.voteAverage ?: 0f,
                    posterPath = movie.posterPath ?: "",
                    mediaType = MediaType.TV.name,
                    type = ShowType.FAVORITE_SHOW.name,
                    addedAt = LocalDateTime.now()
                )
            )
        }

        return remoteReutlt
    }

    override suspend fun getMovieCategory(category: Category): List<ShowDb> {
        val response = when (category) {
            UPCOMING -> movieApi.getUpcoming()
            TRENDING -> movieApi.getTrending()
            POPULAR -> movieApi.getPopular()
            ANIME -> movieApi.getAnimation()
        }

        val result = response.shows?.filterNotNull()?.map {
            it.toDb(
                addedAt = LocalDateTime.now(),
                type = category.toShowType().name,
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
                type = category.toShowType().name,
                mediaType = MediaType.TV,
            )
        }.orEmpty()

        return result
    }


    override suspend fun addToFavorite(id: Int) = remoteUserDataSource.addToFavorite(id)

    override suspend fun deleteFromFavorite(id: Int) = remoteUserDataSource.deleteFromFavorite(id)

    override suspend fun isFavorite(id: Int): Result<Boolean> = remoteUserDataSource.isFavorite(id)

    override fun setReference(type: Types) = remoteUserDataSource.setReference(type)


}