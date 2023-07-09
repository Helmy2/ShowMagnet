package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.user.UserData
import com.example.showmagnet.domain.model.user.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateProfileName(name: String): Result<Boolean>

    suspend fun getUserInfo(): Result<UserData>

    val userPreferencesFlow: Flow<UserPreferences>
}