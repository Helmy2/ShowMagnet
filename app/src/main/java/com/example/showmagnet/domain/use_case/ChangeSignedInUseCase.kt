package com.example.showmagnet.domain.use_case

import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class ChangeSignedInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(isSignedIn: Boolean) {
        return userRepository.setIsSignedIn(isSignedIn)
    }
}