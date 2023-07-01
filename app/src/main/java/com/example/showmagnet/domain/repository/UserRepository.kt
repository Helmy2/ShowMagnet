package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.UserData
import com.example.showmagnet.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateProfileName(name: String): Result<Boolean>

    suspend fun signOut(): Result<Boolean>
    suspend fun getUserInfo(): Result<UserData>

    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun setIsSignedIn(isSignedIn: Boolean): Result<Boolean>
}