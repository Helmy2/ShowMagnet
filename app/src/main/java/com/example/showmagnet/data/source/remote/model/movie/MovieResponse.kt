package com.example.showmagnet.data.source.remote.model.movie

import com.example.showmagnet.data.source.remote.model.common.GenreDto
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("adult") val adult: Boolean?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("belongs_to_collection") val belongsToCollection: BelongsToCollectionDto?,
    @SerializedName("genres") val genres: List<GenreDto>,
    @SerializedName("overview") val overview: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("vote_average") val voteAverage: Float?
)






