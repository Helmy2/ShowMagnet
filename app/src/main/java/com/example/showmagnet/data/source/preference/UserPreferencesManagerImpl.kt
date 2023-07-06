package com.example.showmagnet.data.source.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.showmagnet.domain.model.user.UserPreferences
import com.example.showmagnet.domain.source.preference.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesManager {
    companion object {
        private object PreferencesKeys {
            val IsUserSignedIN =
                booleanPreferencesKey("IsUserSignedIN")
        }
    }

    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    override suspend fun updateIsUserSignedIn(isSignedIn: Boolean) = try {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IsUserSignedIN] = isSignedIn
        }
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }


    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val isOnboardingComplete = preferences[PreferencesKeys.IsUserSignedIN] ?: false
        return UserPreferences(isOnboardingComplete)
    }
}