package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsUserSingedInUserCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userRepository.userPreferencesFlow.map { it.isUserSignedIn }
    }
}