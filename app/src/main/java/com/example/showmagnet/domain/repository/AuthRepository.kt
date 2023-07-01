package com.example.showmagnet.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<Boolean>

    suspend fun signIn(email: String, password: String): Result<Boolean>

    suspend fun signInWithGoogle(): IntentSender?
    fun buildSignInRequest(): BeginSignInRequest

    suspend fun signInWithIntent(intent: Intent): Result<Boolean>

    suspend fun resetPassword(email: String): Result<Boolean>
    fun isSignedIn(): Result<Boolean>
}