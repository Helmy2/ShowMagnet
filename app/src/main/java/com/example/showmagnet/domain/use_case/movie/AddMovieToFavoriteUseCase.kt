package com.example.showmagnet.domain.use_case.movie

import com.example.showmagnet.domain.repository.MovieRepository
import javax.inject.Inject

class AddMovieToFavoriteUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(id: Int) = movieRepository.addMovieToFavoriteList(id)
}