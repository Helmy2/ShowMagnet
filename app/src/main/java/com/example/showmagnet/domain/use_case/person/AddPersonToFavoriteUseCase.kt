package com.example.showmagnet.domain.use_case.person

import com.example.showmagnet.domain.repository.PersonRepository
import javax.inject.Inject

class AddPersonToFavoriteUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    suspend operator fun invoke(id: Int) = personRepository.addPersonToFavoriteList(id)
}