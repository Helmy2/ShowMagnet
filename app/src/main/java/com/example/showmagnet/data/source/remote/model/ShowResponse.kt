package com.example.showmagnet.data.source.remote.model

import com.example.showmagnet.common.Constants
import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.Show
import com.google.gson.annotations.SerializedName

data class ShowResponse(
    val page: String,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val shows: List<ShowRemote>
)


fun ShowResponse.toListShow(): List<Show> {
    return shows.map { show ->
        Show(
            id = show.id,
            title = if (show.mediaType == MediaType.MOVIE.name) show.title ?: "" else show.name
                ?: "",
            voteAverage = show.voteAverage,
            posterPath = show.posterPath ?: "",
            type =
            MediaType.values().firstOrNull { it.value == show.mediaType }
                ?: MediaType.MOVIE
        )
    }
}

fun ShowResponse.toListShow(mediaType: MediaType): List<Show> {
    return shows.map {
        Show(
            id = it.id,
            title = if (mediaType == MediaType.MOVIE) it.title ?: "" else it.name ?: "",
            voteAverage = it.voteAverage,
            posterPath = Constants.IMAGE_URL_W500 + it.posterPath,
            type = mediaType
        )
    }
}