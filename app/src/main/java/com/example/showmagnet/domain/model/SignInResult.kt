package com.example.showmagnet.domain.model

data class SignInResult(
    val isSignedIn: Boolean,
    val errorMessage: String? = null
)