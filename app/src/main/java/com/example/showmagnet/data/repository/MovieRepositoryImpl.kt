package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.remote.api.MovieApi
import com.example.showmagnet.data.source.remote.database.RemoteDataSource
import com.example.showmagnet.data.source.remote.database.Types
import com.example.showmagnet.domain.model.common.Cast
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.movie.Movie
import com.example.showmagnet.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val api: MovieApi,
) : MovieRepository {
    init {
        remoteDataSource.setReference(Types.MOVIES)
    }

    override suspend fun getMovieDetails(id: Int): Result<Movie> = try {
        val response = api.getMovieDetails(id)
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

    override suspend fun getCast(id: Int): Result<List<Cast>> = try {
        val response = api.getMovieCast(id)
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

    override suspend fun getCollection(id: Int): Result<List<Show>> = try {
        val response = api.getMovieCollection(id)
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

    override suspend fun getImages(id: Int): Result<List<Image>> = try {
        val response = api.getMovieImages(id)
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
        val response = api.getMovieRecommendations(id)
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

    override suspend fun getUpcoming(): Result<List<Show>> = try {
        val response = api.getUpcomingMovie()
        val result = response.shows?.filterNotNull()?.map { it.toDomain(MediaType.MOVIE) }

        Result.success(result)

        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getPopular(): Result<List<Show>> = try {
        val response = api.getPopularMovies()

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

    override suspend fun discoverMovie(parameters: Map<String, String>): Result<List<Show>> = try {
        val response = api.discoverMovie(parameters)

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

    override suspend fun getFavoriteMovies(): Result<List<Show>> = try {
        val favoriteList = getMoviesFavoriteList().getOrThrow()
        val list = mutableListOf<Show>()

        favoriteList.forEach {
            val movie = api.getMovieDetails(it)
            list.add(
                Show(
                    id = movie.id,
                    title = movie.title.orEmpty(),
                    voteAverage = movie.voteAverage ?: 0f,
                    posterPath = Image(movie.posterPath),
                    type = MediaType.MOVIE
                )
            )
        }

        Result.success(list)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override suspend fun addMovieToFavoriteList(id: Int) = remoteDataSource.addToFavorite(id)

    override suspend fun getMoviesFavoriteList() = remoteDataSource.getFavoriteList()

    override suspend fun deleteFromFavoriteMovieList(id: Int) =
        remoteDataSource.deleteFromFavorite(id)

    override suspend fun isFavoriteMovie(id: Int) = remoteDataSource.isFavorite(id)
}

