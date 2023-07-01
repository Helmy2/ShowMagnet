package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val changeIsUserSignedInUseCase: ChangeIsUserSignedInUseCase
) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        var result = authRepository.signIn(email, password)
        if (result.isSuccess)
            result = changeIsUserSignedInUseCase(true)
        return result
    }
}