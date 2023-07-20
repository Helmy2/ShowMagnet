package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.repository.MovieRepository
import com.example.showmagnet.domain.repository.TvRepository
import javax.inject.Inject

class RefreshCategoryUseCase @Inject constructor(
    private val movieRepository: MovieRepository, private val tvRepository: TvRepository
) {
    suspend operator fun invoke(category: Category, mediaType: MediaType) = when (mediaType) {
        MediaType.MOVIE -> movieRepository.refreshCategory(category)
        MediaType.TV -> tvRepository.refreshCategory(category)
    }
}