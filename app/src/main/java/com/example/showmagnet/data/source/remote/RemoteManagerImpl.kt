package com.example.showmagnet.data.source.remote

import com.example.showmagnet.data.mapper.toDb
import com.example.showmagnet.data.source.local.model.PeopleType
import com.example.showmagnet.data.source.local.model.PeopleType.FAVORITE_PEOPLE
import com.example.showmagnet.data.source.local.model.PeopleType.POPULAR_PEOPLE
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.remote.api.MovieApi
import com.example.showmagnet.data.source.remote.api.PersonApi
import com.example.showmagnet.data.source.remote.api.TvApi
import com.example.showmagnet.data.source.remote.api.model.common.CreditsResponse
import com.example.showmagnet.data.source.remote.api.model.common.ShowResponse
import com.example.showmagnet.data.source.remote.api.model.movie.CollectionResponse
import com.example.showmagnet.data.source.remote.api.model.movie.ImagesResponse
import com.example.showmagnet.data.source.remote.api.model.movie.MovieResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonCreditResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonDetailsResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonImagesResponse
import com.example.showmagnet.data.source.remote.api.model.tv.SeasonResponse
import com.example.showmagnet.data.source.remote.api.model.tv.TvResponse
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

    override suspend fun getPeople(
        type: PeopleType, timeWindow: TimeWindow
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
            UPCOMING -> movieApi.getUpcoming()
            TRENDING -> movieApi.getTrending()
            POPULAR -> movieApi.getPopular()
            ANIME -> movieApi.getAnimation()
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

    override suspend fun getPersonDetails(id: Int): PersonDetailsResponse =
        personApi.getPersonDetails(id)

    override suspend fun getPersonImages(id: Int): PersonImagesResponse =
        personApi.getPersonImages(id)

    override suspend fun getMovieCredits(id: Int): PersonCreditResponse =
        personApi.getMovieCredits(id)

    override suspend fun getTvCredits(id: Int): PersonCreditResponse = personApi.getTvCredits(id)

    override suspend fun addToFavorite(id: Int) = remoteUserDataSource.addToFavorite(id)

    override suspend fun deleteFromFavorite(id: Int) = remoteUserDataSource.deleteFromFavorite(id)
    override suspend fun getFavoriteList(): Result<List<Int>> =
        remoteUserDataSource.getFavoriteList()

    override suspend fun isFavorite(id: Int): Result<Boolean> = remoteUserDataSource.isFavorite(id)

    override fun setReference(type: Types) = remoteUserDataSource.setReference(type)
    override suspend fun getTvDetails(id: Int): TvResponse = tvApi.getDetails(id)

    override suspend fun getTvSeason(id: Int, seasonNumber: Int): SeasonResponse =
        tvApi.getSeason(id, seasonNumber)

    override suspend fun getTvCast(id: Int): CreditsResponse = tvApi.getCast(id)

    override suspend fun getTvImages(id: Int): ImagesResponse = tvApi.getImages(id)

    override suspend fun getTvRecommendations(id: Int): ShowResponse = tvApi.getRecommendations(id)

    override suspend fun searchTv(query: String, page: Int): ShowResponse =
        tvApi.search(query, page)

    override suspend fun discoverTv(parameters: Map<String, String>): ShowResponse =
        tvApi.discoverTv(parameters)

    override suspend fun getMovieDetails(id: Int): MovieResponse = movieApi.getDetails(id)

    override suspend fun getMovieCast(id: Int): CreditsResponse = movieApi.getCast(id)
    override suspend fun getMovieCollection(id: Int): CollectionResponse = movieApi.getCollection(id)

    override suspend fun getMovieImages(id: Int): ImagesResponse = movieApi.getImages(id)

    override suspend fun getMovieRecommendations(id: Int): ShowResponse = movieApi.getRecommendations(id)

    override suspend fun discoverMovie(parameters: Map<String, String>): ShowResponse =
        movieApi.discover(parameters)

    override suspend fun searchMovie(query: String, page: Int): ShowResponse = movieApi.search(query, page)

}