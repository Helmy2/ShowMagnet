package com.example.showmagnet.data.source.remote.model.tv

import com.example.showmagnet.data.source.remote.model.GenreRemote
import com.example.showmagnet.data.source.remote.model.toGenre
import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.Tv
import com.google.gson.annotations.SerializedName

data class TvResponse(
    val id: Int,
    val name: String,
    val adult: Boolean?,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val genres: List<GenreRemote>,
    val overview: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?
)


fun TvResponse.toTv() = Tv(
    id = id,
    name = name,
    adult = adult ?: false,
    numberOfEpisodes = numberOfEpisodes,
    numberOfSeasons = numberOfSeasons,
    backdropPath = Image(backdropPath),
    posterPath = Image(posterPath),
    genres = genres.map { it.toGenre() },
    overview = overview ?: "",
    firstAirDate = firstAirDate ?: "",
    voteAverage = voteAverage
)