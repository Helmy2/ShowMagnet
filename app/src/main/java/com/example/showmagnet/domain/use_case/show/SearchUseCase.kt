package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.repository.MovieRepository
import com.example.showmagnet.domain.repository.TvRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    suspend operator fun invoke(
        page: Int,
        mediaType: MediaType,
        query: String,
    ) = when (mediaType) {
        MediaType.MOVIE -> movieRepository.search(query, page)
        MediaType.TV -> tvRepository.search(query, page)
    }
}


