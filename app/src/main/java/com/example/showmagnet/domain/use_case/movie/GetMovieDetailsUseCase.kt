package com.example.showmagnet.domain.use_case.movie

import com.example.showmagnet.domain.model.movie.Movie
import com.example.showmagnet.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(id: Int): Result<Movie> {
        return movieRepository.getMovieDetails(id)
    }
}