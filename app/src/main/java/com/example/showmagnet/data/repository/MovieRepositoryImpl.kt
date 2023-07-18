package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.mapper.toShowType
import com.example.showmagnet.data.source.local.LocalManager
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.data.source.local.model.ShowType
import com.example.showmagnet.data.source.remote.RemoteManager
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.di.IoDispatcher
import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.movie.Movie
import com.example.showmagnet.domain.repository.MovieRepository
import com.example.showmagnet.utils.handleErrors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val localManager: LocalManager,
    private val remoteManager: RemoteManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MovieRepository {
    init {
        remoteManager.setReference(Types.MOVIES)
    }

    override suspend fun getMovieDetails(id: Int): Result<Movie> = withContext(ioDispatcher) {
        try {
            val response = remoteManager.getMovieDetails(id)
            val favorite = isFavoriteMovie(response.id)
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
            val response = remoteManager.getMovieCast(id)
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
            val response = remoteManager.getMovieCollection(id)
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
            val response = remoteManager.getMovieImages(id)
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
                val response = remoteManager.getMovieRecommendations(id)
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

    override fun getCategory(category: Category): Flow<Result<List<Show>>> = flow {
        val localResult = localManager.getShows(category.toShowType(), MediaType.MOVIE)
        emit(Result.success(localResult.map { it.toDomain() }))

        val remoteReutlt = remoteManager.getMovieCategory(category)
        emit(Result.success(remoteReutlt.map { it.toDomain() }))

        localManager.deleteShows(category.toShowType(), MediaType.MOVIE)
        localManager.insertShow(remoteReutlt)
    }.flowOn(ioDispatcher).handleErrors()


    override suspend fun getFavorite(): Flow<Result<List<Show>>> = flow {
        val localResult = localManager.getShows(ShowType.FAVORITE_SHOW, MediaType.MOVIE)
        emit(Result.success(localResult.map { it.toDomain() }))

        val favoriteList = getMoviesFavoriteList().getOrThrow()

        val remoteReutlt = mutableListOf<ShowDb>()
        favoriteList.forEach {
            val movie = remoteManager.getMovieDetails(it)
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
        emit(Result.success(remoteReutlt.map { it.toDomain() }))

        localManager.deleteShows(ShowType.FAVORITE_SHOW, MediaType.MOVIE)
        localManager.insertShow(remoteReutlt)
    }.flowOn(ioDispatcher).handleErrors()

    override suspend fun discoverMovie(parameters: Map<String, String>): Result<List<Show>> =
        withContext(ioDispatcher) {
            try {
                val response = remoteManager.discoverMovie(parameters)

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
                val response = remoteManager.searchMovie(query, page)
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

    override suspend fun getMoviesFavoriteList() = remoteManager.getFavoriteList()

    override suspend fun deleteFromFavoriteMovieList(id: Int) = remoteManager.deleteFromFavorite(id)

    override suspend fun isFavoriteMovie(id: Int) = remoteManager.isFavorite(id)
}

