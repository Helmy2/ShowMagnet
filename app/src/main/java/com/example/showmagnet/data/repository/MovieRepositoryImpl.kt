package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.mapper.toShowType
import com.example.showmagnet.data.source.local.LocalManager
import com.example.showmagnet.data.source.local.model.ShowType
import com.example.showmagnet.data.source.remote.RemoteManager
import com.example.showmagnet.data.source.remote.api.MovieApi
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.di.IoDispatcher
import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.movie.Movie
import com.example.showmagnet.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val localManager: LocalManager,
    private val remoteManager: RemoteManager,
    private val movieApi: MovieApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MovieRepository {
    init {
        remoteManager.setReference(Types.MOVIES)
    }

    override suspend fun getMovieDetails(id: Int): Result<Movie> = withContext(ioDispatcher) {
        try {
            val response = movieApi.getDetails(id)
            val favorite = remoteManager.isFavorite(response.id)
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
    }

    override suspend fun getCast(id: Int): Result<List<Cast>> = withContext(ioDispatcher) {
        try {
            val response = movieApi.getCast(id)
            val result = response.cast?.filterNotNull()?.filter { it.profilePath != null }
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
    }

    override suspend fun getCollection(id: Int): Result<List<Show>> = withContext(ioDispatcher) {
        try {
            val response = movieApi.getCollection(id)
            val result = response.shows?.filterNotNull()?.filter { it.posterPath != null }
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
    }

    override suspend fun getImages(id: Int): Result<List<Image>> = withContext(ioDispatcher) {
        try {
            val response = movieApi.getImages(id)
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
    }

    override suspend fun getRecommendations(id: Int): Result<List<Show>> =
        withContext(ioDispatcher) {
            try {
                val response = movieApi.getRecommendations(id)
                val result = response.shows?.filterNotNull()?.filter { it.posterPath != null }
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
        }

    override fun getCategory(category: Category): Flow<List<Show>> =
        localManager.getShows(category.toShowType())
            .distinctUntilChanged()
            .map { list -> list.map { it.toDomain() } }.flowOn(ioDispatcher)

    override suspend fun refreshCategory(category: Category) {
        val remoteReutlt = remoteManager.getMovieCategory(category)

        localManager.refreshShow(remoteReutlt, category.toShowType(), MediaType.MOVIE)
    }


    override suspend fun getFavorite(): Flow<List<Show>> =
        localManager.getShows(ShowType.FAVORITE_SHOW)
            .distinctUntilChanged()
            .map { list -> list.map { it.toDomain() } }
            .map { it.filter { it.mediaType == MediaType.MOVIE } }
            .flowOn(ioDispatcher)

    override suspend fun refreshFavorite() {
        val remoteReutlt = remoteManager.getFavoriteMovies()

        localManager.refreshShow(remoteReutlt, ShowType.FAVORITE_SHOW, MediaType.MOVIE)
    }

    override suspend fun discoverMovie(parameters: Map<String, String>): Result<List<Show>> =
        withContext(ioDispatcher) {
            try {
                val response = movieApi.discover(parameters)

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
        }

    override suspend fun search(query: String, page: Int): Result<List<Show>> =
        withContext(ioDispatcher) {
            try {
                val response = movieApi.search(query, page)
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
        }


    override suspend fun addMovieToFavoriteList(id: Int) = remoteManager.addToFavorite(id)

    override suspend fun deleteFromFavoriteMovieList(id: Int) = remoteManager.deleteFromFavorite(id)

}

