package com.example.showmagnet.domain.source.preference

import com.example.showmagnet.data.source.preference.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesManager {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun setIsUserSignedIn(isSignedIn: Boolean)
}