package com.example.showmagnet.domain.use_case

import com.example.showmagnet.domain.model.SignResult
import com.example.showmagnet.domain.repository.AuthRepository
import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(name: String, email: String, password: String): SignResult {
        val signResult = authRepository.signUp(email, password)
        if (!signResult.success)
            return signResult

        return userRepository.updateProfileName(name)
    }
}