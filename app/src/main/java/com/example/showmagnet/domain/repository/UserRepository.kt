package com.example.showmagnet.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.example.showmagnet.domain.model.user.UserData
import com.example.showmagnet.domain.model.user.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateProfileName(name: String): Result<Boolean>

    suspend fun getUserInfo(): Result<UserData>

    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun signInWithGoogle(): Result<IntentSender?>
    suspend fun signOut(): Result<Boolean>
    suspend fun resetPassword(email: String): Result<Boolean>
    fun isSignedIn(): Result<Boolean>
    suspend fun signInWithIntent(intent: Intent): Result<Boolean>
    suspend fun signIn(email: String, password: String): Result<Boolean>
    suspend fun signUp(name: String, email: String, password: String): Result<Boolean>
}