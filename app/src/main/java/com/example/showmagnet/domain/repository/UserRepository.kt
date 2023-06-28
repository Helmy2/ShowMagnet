package com.example.showmagnet.domain.repository

import com.example.showmagnet.data.source.preference.UserPreferences
import com.example.showmagnet.domain.model.SignResult
import com.example.showmagnet.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateProfileName(name: String): SignResult

    suspend fun signOut(): Boolean
    suspend fun getUserInfo(): Result<UserData>
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun setIsSignedIn(isSignedIn: Boolean)
}