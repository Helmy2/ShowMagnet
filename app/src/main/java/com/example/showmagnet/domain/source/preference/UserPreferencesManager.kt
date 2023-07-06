package com.example.showmagnet.domain.source.preference

import com.example.showmagnet.domain.model.user.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesManager {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun updateIsUserSignedIn(isSignedIn: Boolean): Result<Boolean>
}