package com.example.showmagnet.domain.use_case.movie

import com.example.showmagnet.domain.repository.MovieRepository
import javax.inject.Inject

class GetCollectionUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(collectionId: Int) = movieRepository.getCollection(collectionId)
}