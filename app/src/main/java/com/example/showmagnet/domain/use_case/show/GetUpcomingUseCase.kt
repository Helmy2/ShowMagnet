package com.example.showmagnet.domain.use_case.show

import com.example.showmagnet.domain.repository.ShowRepository
import javax.inject.Inject

class GetUpcomingUseCase @Inject constructor(
    private val showRepository: ShowRepository
) {
    suspend operator fun invoke() =
        showRepository.getUpcoming()
}