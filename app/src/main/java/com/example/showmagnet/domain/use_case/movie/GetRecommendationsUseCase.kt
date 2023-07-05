package com.example.showmagnet.domain.use_case.movie

import com.example.showmagnet.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val movieRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(id: Int) = movieRepository.getRecommendations(id)
}