package com.example.showmagnet.domain.use_case.favorite

import com.example.showmagnet.domain.repository.MovieRepository
import javax.inject.Inject

class GetFavoriteMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() = repository.getFavoriteMovies()
}