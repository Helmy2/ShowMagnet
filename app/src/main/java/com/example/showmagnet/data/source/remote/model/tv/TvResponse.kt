package com.example.showmagnet.data.source.remote.model.tv

import com.example.showmagnet.data.source.remote.model.common.GenreDto
import com.google.gson.annotations.SerializedName

data class TvResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("adult") val adult: Boolean?,
    @SerializedName("number_of_episodes") val numberOfEpisodes: Int?,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("genres") val genres: List<GenreDto?>?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Float?
)


