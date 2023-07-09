package com.example.showmagnet.domain.use_case.auth

import android.content.Intent
import com.example.showmagnet.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(intent: Intent): Result<Boolean> {
        return authRepository.signInWithIntent(intent)
    }
}