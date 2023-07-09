package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.repository.MovieRepository
import javax.inject.Inject

class GetUpcomingUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke() =
        movieRepository.getUpcoming()
}