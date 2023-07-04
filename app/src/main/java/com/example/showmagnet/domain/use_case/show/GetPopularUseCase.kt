package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.repository.HomeRepository
import javax.inject.Inject

class GetPopularUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(mediaType: MediaType) =
        homeRepository.getPopular(mediaType)
}
