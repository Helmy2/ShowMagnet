package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.model.TimeWindow
import com.example.showmagnet.domain.repository.ShowRepository
import javax.inject.Inject

class GetTrendingUseCase @Inject constructor(
    private val showRepository: ShowRepository
) {
    suspend operator fun invoke(timeWindow: TimeWindow) =
        showRepository.getTrending(timeWindow)
}