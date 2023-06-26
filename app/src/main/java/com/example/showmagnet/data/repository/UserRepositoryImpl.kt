package com.example.showmagnet.data.repository

import com.example.showmagnet.domain.model.SignResult
import com.example.showmagnet.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
) : UserRepository {
    override suspend fun updateProfileName(name: String): SignResult = try {
        val user = auth.currentUser
        user?.updateProfile(userProfileChangeRequest { displayName = name })
            ?.await()

        SignResult(true)
    } catch (e: Exception) {
        SignResult(false, e.message)
    }
}