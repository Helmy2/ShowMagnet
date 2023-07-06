package com.example.showmagnet.domain.use_case.tv

import com.example.showmagnet.domain.repository.TvDetailsRepository
import javax.inject.Inject

class GetTvRecommendationsUseCase @Inject constructor(
    private val tvDetailsRepository: TvDetailsRepository
) {
    suspend operator fun invoke(id: Int) = tvDetailsRepository.getRecommendations(id)
}