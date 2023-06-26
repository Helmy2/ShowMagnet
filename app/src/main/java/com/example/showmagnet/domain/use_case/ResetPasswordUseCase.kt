package com.example.showmagnet.domain.use_case

import com.example.showmagnet.domain.model.SignResult
import com.example.showmagnet.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): SignResult {
        return authRepository.resetPassword(email)
    }
}