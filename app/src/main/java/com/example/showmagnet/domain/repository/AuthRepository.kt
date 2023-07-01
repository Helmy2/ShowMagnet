package com.example.showmagnet.domain.repository

import android.content.Intent
import android.content.IntentSender

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<Boolean>

    suspend fun signIn(email: String, password: String): Result<Boolean>

    suspend fun signInWithGoogle(): Result<IntentSender?>

    suspend fun signInWithIntent(intent: Intent): Result<Boolean>

    suspend fun resetPassword(email: String): Result<Boolean>
    fun isSignedIn(): Result<Boolean>
}