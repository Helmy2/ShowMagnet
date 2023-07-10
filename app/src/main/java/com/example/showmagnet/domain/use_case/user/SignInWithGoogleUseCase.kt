package com.example.showmagnet.domain.use_case.user

import android.content.Intent
import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(intent: Intent): Result<Boolean> =
        userRepository.signInWithIntent(intent)
}