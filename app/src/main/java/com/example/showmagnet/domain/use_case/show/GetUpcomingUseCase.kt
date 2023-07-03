package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.repository.HomeRepository
import javax.inject.Inject

class GetUpcomingUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() =
        homeRepository.getUpcoming()
}