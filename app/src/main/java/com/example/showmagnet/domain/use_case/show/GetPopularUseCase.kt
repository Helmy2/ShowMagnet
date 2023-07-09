package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: MovieRepository
) {
    suspend operator fun invoke(mediaType: MediaType) =
        when (mediaType) {
            MediaType.MOVIE -> movieRepository.getPopular()
            MediaType.TV -> tvRepository.getPopular()
        }
}

