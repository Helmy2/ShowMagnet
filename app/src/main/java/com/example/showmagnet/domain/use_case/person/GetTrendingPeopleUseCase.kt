package com.example.showmagnet.domain.use_case.person

import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrendingPeopleUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    operator fun invoke(timeWindow: TimeWindow): Flow<List<Person>> =
        personRepository.getTrendingPeople(timeWindow)

}