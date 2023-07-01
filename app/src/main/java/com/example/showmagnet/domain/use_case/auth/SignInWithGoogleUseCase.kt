package com.example.showmagnet.domain.use_case.auth

import android.content.Intent
import com.example.showmagnet.domain.model.SignResult
import com.example.showmagnet.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val changeIsUserSignedInUseCase: ChangeIsUserSignedInUseCase
) {

    suspend operator fun invoke(intent: Intent): SignResult {
        val result = authRepository.signInWithIntent(intent)
        if (result.success)
            changeIsUserSignedInUseCase(true)
        return result
    }
}