package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class GetGoogleIntentUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() =
        userRepository.signInWithGoogle()

}