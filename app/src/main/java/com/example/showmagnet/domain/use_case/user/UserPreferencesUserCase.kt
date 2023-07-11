package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class UserPreferencesUserCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() = userRepository.userPreferencesFlow

}