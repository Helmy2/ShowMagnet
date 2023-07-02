package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.TimeWindow
import com.example.showmagnet.domain.repository.HomeRepository
import javax.inject.Inject

class GetTrendingUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(timeWindow: TimeWindow) =
        homeRepository.getTrending(timeWindow)
}