package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class ChangeIsUserSignedInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(isSignedIn: Boolean) =
        userRepository.setIsSignedIn(isSignedIn)
}