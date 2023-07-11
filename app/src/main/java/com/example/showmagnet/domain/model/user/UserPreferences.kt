package com.example.showmagnet.domain.model.user

data class UserPreferences(
    val isUserSignedIn: Boolean = false,
    val darkTheme: Boolean = false
)