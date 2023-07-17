package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.mapper.toDb
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
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
    private val personApi: PersonApi,
    private val tvApi: TvApi,
    private val movieApi: MovieApi,
) : RemoteManager {
    override suspend fun getTrendingPeople(
        timeWindow: TimeWindow
    ): List<PersonDb> {
        val now = LocalDateTime.now()

        return personApi.getTrendingPeople(timeWindow.value).results?.filterNotNull()
            ?.filter { it.profilePath != null }?.map { it.toDb(timeWindow, now) } ?: emptyList()
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
                categoryName = category.name,
                type = MediaType.MOVIE,
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
                    categoryName = category.name,
                    type = MediaType.TV,
                )
            }.orEmpty()

        return result
    }
}