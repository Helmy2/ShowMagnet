package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) =
        authRepository.resetPassword(email)

}