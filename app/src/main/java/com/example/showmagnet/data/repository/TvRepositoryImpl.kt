package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.local.LocalManager
import com.example.showmagnet.data.source.remote.api.RemoteManager
import com.example.showmagnet.data.source.remote.api.TvApi
import com.example.showmagnet.data.source.remote.database.RemoteUserDataSource
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.tv.Episode
import com.example.showmagnet.domain.model.tv.Tv
import com.example.showmagnet.domain.repository.TvRepository
import com.example.showmagnet.utils.repositoryFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TvRepositoryImpl @Inject constructor(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localManager: LocalManager,
    private val remoteManager: RemoteManager,
    private val api: TvApi,
) : TvRepository {

    init {
        remoteUserDataSource.setReference(Types.TVS)
    }


    override suspend fun getDetails(id: Int): Result<Tv> = try {
        val response = api.getDetails(id)
        val favorite = isFavoriteTv(response.id)
        val result = if (favorite.isSuccess) {
            response.toDomain(favorite = favorite.getOrThrow())
        } else {
            response.toDomain(false)
        }

        Result.success(result)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getSeason(id: Int, seasonNumber: Int): Result<List<Episode>> = try {
        val response = api.getSeason(id, seasonNumber)
        val result = response.episodes?.filterNotNull()?.filter { it.voteAverage != 0.0 }
            ?.map { it.toDomain() }


        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getCast(id: Int): Result<List<Cast>> = try {
        val response = api.getCast(id)

        val result =
            response.cast?.filterNotNull()?.filter { it.profilePath != null }?.map { it.toDomain() }


        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override suspend fun getImages(id: Int): Result<List<Image>> = try {
        val response = api.getImages(id)
        val result = (response.backdrops.orEmpty() + response.posters.orEmpty()).filterNotNull()
            .map { it.toDomain() }

        if (result.isEmpty()) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getRecommendations(id: Int): Result<List<Show>> = try {
        val response = api.getRecommendations(id)
        val result =
            response.shows?.filterNotNull()?.filter { it.posterPath != null }?.map { it.toDomain() }


        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }



    override fun getCategory(category: Category): Flow<Result<List<Show>>> =
        repositoryFlow(
            localFlow = localManager.getCategory(category)
                .map { list -> list.map { it.toDomain() } },
            updateFun = {
                val result = remoteManager.getTvCategory(category)
                localManager.insertShow(result)
            },
        )


    override suspend fun getFavoriteTv(): Result<List<Show>> = try {
        val favoriteList = getTvFavoriteList().getOrThrow()
        val list = mutableListOf<Show>()

        favoriteList.forEach {
            val tv = api.getDetails(it)
            list.add(
                Show(
                    id = tv.id,
                    title = tv.name.orEmpty(),
                    voteAverage = tv.voteAverage ?: 0f,
                    posterPath = Image(tv.posterPath),
                    type = MediaType.TV
                )
            )
        }

        Result.success(list)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun discoverTv(parameters: Map<String, String>): Result<List<Show>> = try {
        val response = api.discoverTv(parameters)

        val result = response.shows?.filterNotNull()?.map { it.toDomain(MediaType.TV) }

        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun search(query: String, page: Int): Result<List<Show>> = try {
        val response = api.search(query, page)
        val result = response.shows?.filterNotNull()?.map { it.toDomain(MediaType.MOVIE) }
        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override suspend fun addTvToFavoriteList(id: Int) = remoteUserDataSource.addToFavorite(id)

    override suspend fun getTvFavoriteList() = remoteUserDataSource.getFavoriteList()

    override suspend fun deleteFromFavoriteTvList(id: Int) =
        remoteUserDataSource.deleteFromFavorite(id)

    override suspend fun isFavoriteTv(id: Int) = remoteUserDataSource.isFavorite(id)
}

