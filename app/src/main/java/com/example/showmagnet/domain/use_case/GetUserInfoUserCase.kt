package com.example.showmagnet.domain.use_case

import com.example.showmagnet.domain.model.UserData
import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUserCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserData> {
        return userRepository.getUserInfo()
    }
}