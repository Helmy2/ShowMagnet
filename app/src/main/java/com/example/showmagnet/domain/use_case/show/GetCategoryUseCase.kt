package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.repository.MovieRepository
import com.example.showmagnet.domain.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    operator fun invoke(category: Category, mediaType: MediaType): Flow<List<Show>> = when (mediaType) {
        MediaType.MOVIE -> movieRepository.getCategory(category)
        MediaType.TV -> tvRepository.getCategory(category)
    }
}