package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.repository.MovieRepository
import com.example.showmagnet.domain.repository.TvRepository
import javax.inject.Inject

class GetPopularUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    suspend operator fun invoke(mediaType: MediaType) =
        when (mediaType) {
            MediaType.MOVIE -> movieRepository.getPopular()
            MediaType.TV -> tvRepository.getPopular()
        }
}

