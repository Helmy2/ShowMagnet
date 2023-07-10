package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Boolean> =
        userRepository.signUp(name, email, password)
}