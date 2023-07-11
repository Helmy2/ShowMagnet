package com.example.showmagnet.domain.use_case.user

import android.graphics.Bitmap
import com.example.showmagnet.domain.repository.UserRepository
import javax.inject.Inject

class UpdateProfileUseCase  @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, bitmap: Bitmap?) = userRepository.updateProfile(name, bitmap)

}