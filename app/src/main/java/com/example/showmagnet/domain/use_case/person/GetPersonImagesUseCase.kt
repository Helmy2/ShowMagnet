package com.example.showmagnet.domain.use_case.person

import com.example.showmagnet.domain.repository.PersonRepository
import javax.inject.Inject

class GetPersonImagesUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    suspend operator fun invoke(id: Int) = personRepository.getImages(id)
}