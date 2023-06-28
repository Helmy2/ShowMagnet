package com.example.showmagnet.data.repository

import com.example.showmagnet.data.source.preference.UserPreferences
import com.example.showmagnet.domain.model.SignResult
import com.example.showmagnet.domain.model.UserData
import com.example.showmagnet.domain.repository.UserRepository
import com.example.showmagnet.domain.source.preference.UserPreferencesManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val userPreferencesManager: UserPreferencesManager
) : UserRepository {

    override val userPreferencesFlow: Flow<UserPreferences> =
        userPreferencesManager.userPreferencesFlow

    override suspend fun updateProfileName(name: String): SignResult = try {
        val user = auth.currentUser
        user?.updateProfile(userProfileChangeRequest { displayName = name })
            ?.await()

        SignResult(true)
    } catch (e: Exception) {
        SignResult(false, e.message)
    }

    override suspend fun signOut(): Boolean {
        try {
            auth.signOut()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
        return false
    }

    override suspend fun getUserInfo(): Result<UserData> = try {
        val user = auth.currentUser
        if (user == null)
            Result.failure(Exception("User not found"))
        else
            Result.success(
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

    override suspend fun setIsSignedIn(isSignedIn: Boolean) {
        userPreferencesManager.setIsUserSignedIn(isSignedIn)
    }
}