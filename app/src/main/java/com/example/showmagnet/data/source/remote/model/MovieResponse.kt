package com.example.showmagnet.data.source.remote.model

import com.example.showmagnet.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollectionRemote?,
    @SerializedName("genres")
    val genres: List<GenreRemote>,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?
)

fun MovieResponse.toMovie() = Movie(
    id = id,
    title = title,
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    posterPath = posterPath ?: "",
    genres = genres.map { it.toGenre() },
    overview = overview ?: "",
    releaseDate = releaseDate ?: "",
    runtime = runtime ?: 0,
    status = status ?: "",
    voteAverage = voteAverage ?: 0.0,
    collectionName = belongsToCollection?.name,
    collectionId = belongsToCollection?.id,
)




