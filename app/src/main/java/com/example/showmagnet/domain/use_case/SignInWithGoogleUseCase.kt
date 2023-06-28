package com.example.showmagnet.domain.use_case

import android.content.Intent
import android.content.IntentSender
import com.example.showmagnet.domain.model.SignResult
import com.example.showmagnet.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val changeSignedInUseCase: ChangeSignedInUseCase
) {

    suspend fun getIntentSender(): IntentSender? {
        return authRepository.signInWithGoogle()
    }

    suspend operator fun invoke(intent: Intent): SignResult {
        val result = authRepository.signInWithIntent(intent)
        if (result.success)
            changeSignedInUseCase(true)
        return result
    }
}