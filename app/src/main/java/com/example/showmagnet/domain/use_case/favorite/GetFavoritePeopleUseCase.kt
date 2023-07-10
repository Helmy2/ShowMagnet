package com.example.showmagnet.domain.use_case.favorite

import com.example.showmagnet.domain.repository.PersonRepository
import javax.inject.Inject

class GetFavoritePeopleUseCase @Inject constructor(
    private val repository: PersonRepository
) {
    suspend operator fun invoke() = repository.getFavoritePeople()
}