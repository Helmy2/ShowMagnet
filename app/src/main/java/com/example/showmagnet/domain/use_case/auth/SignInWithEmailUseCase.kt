package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.domain.model.SignResult
import com.example.showmagnet.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val changeIsUserSignedInUseCase: ChangeIsUserSignedInUseCase
) {
    suspend operator fun invoke(email: String, password: String): SignResult {
        val result = authRepository.signIn(email, password)
        if (result.success)
            changeIsUserSignedInUseCase(true)
        return result
    }
}