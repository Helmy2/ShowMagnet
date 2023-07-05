package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.repository.ShowRepository
import javax.inject.Inject

class GetPopularUseCase @Inject constructor(
    private val showRepository: ShowRepository
) {
    suspend operator fun invoke(mediaType: MediaType) =
        showRepository.getPopular(mediaType)
}

