package com.example.showmagnet.domain.use_case.auth

import android.content.Intent
import com.example.showmagnet.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val changeIsUserSignedInUseCase: ChangeIsUserSignedInUseCase
) {

    suspend operator fun invoke(intent: Intent): Result<Boolean> {
        var result = authRepository.signInWithIntent(intent)
        if (result.isSuccess)
            result = changeIsUserSignedInUseCase(true)
        return result
    }
}