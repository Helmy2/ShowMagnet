package com.example.showmagnet.domain.use_case.person

import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.repository.PersonRepository
import javax.inject.Inject

class RefreshTrendingPeopleUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    suspend operator fun invoke(timeWindow: TimeWindow) = personRepository.refreshTrending(timeWindow)

}