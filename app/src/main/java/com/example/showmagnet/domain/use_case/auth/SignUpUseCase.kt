package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.domain.repository.AuthRepository
import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Boolean> {
        var result = authRepository.signUp(email, password)
        if (result.isSuccess)
            result = userRepository.updateProfileName(name)

        return result
    }
}