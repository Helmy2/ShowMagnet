package com.example.showmagnet.domain.use_case.tv

import com.example.showmagnet.domain.repository.TvRepository
import javax.inject.Inject

class GetTvImagesUseCase @Inject constructor(
    private val tvRepository: TvRepository
) {
    suspend operator fun invoke(id: Int) = tvRepository.getImages(id)
}