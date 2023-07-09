package com.example.showmagnet.domain.use_case.movie

import com.example.showmagnet.domain.repository.MovieRepository
import javax.inject.Inject

class GetCastUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(id: Int) = movieRepository.getCast(id)
}