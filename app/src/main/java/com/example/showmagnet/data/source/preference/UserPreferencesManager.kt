package com.example.showmagnet.data.source.preference

import com.example.showmagnet.domain.model.user.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesManager {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun updateIsUserSignedIn(isSignedIn: Boolean): Result<Boolean>
    suspend fun updateDarkTheme(darkTheme: Boolean): Result<Boolean>
}