package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.SortBy
import com.example.showmagnet.domain.repository.MovieRepository
import com.example.showmagnet.domain.repository.TvRepository
import javax.inject.Inject

class DiscoverUseCase @Inject constructor(
    private val movieRepository: MovieRepository, private val tvRepository: TvRepository
) {
    suspend operator fun invoke(
        page: Int,
        mediaType: MediaType,
        genreId: Int?,
        adult: Boolean,
        sortBy: SortBy,
    ) = when (mediaType) {
        MediaType.MOVIE -> movieRepository.discoverMovie(buildMap {
            if (genreId != null) put("with_genres", genreId.toString())
            put("page", page.toString())
            put("include_adult", adult.toString())
            put("sort_by", sortBy.query())
        })

        MediaType.TV -> tvRepository.discoverTv(buildMap {
            if (genreId != null) put("with_genres", genreId.toString())
            put("page", page.toString())
            put("include_adult", adult.toString())
            put("sort_by", sortBy.query())
        })
    }
}


