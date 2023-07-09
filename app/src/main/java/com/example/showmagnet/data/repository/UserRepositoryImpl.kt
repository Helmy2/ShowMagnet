package com.example.showmagnet.data.repository

import com.example.showmagnet.data.source.preference.UserPreferencesManager
import com.example.showmagnet.domain.model.user.UserData
import com.example.showmagnet.domain.model.user.UserPreferences
import com.example.showmagnet.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userPreferencesManager: UserPreferencesManager
) : UserRepository {

    override val userPreferencesFlow: Flow<UserPreferences> =
        userPreferencesManager.userPreferencesFlow

    override suspend fun updateProfileName(name: String) = try {
        val user = auth.currentUser
        user?.updateProfile(userProfileChangeRequest { displayName = name })?.await()

        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserInfo(): Result<UserData> = try {
        val user = auth.currentUser
        if (user == null) Result.failure(Exception("User not found"))
        else Result.success(
            UserData(
                email = user.email ?: "",
                username = user.displayName ?: "",
                profilePictureUrl = user.photoUrl?.toString()
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}