package com.example.showmagnet.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.example.showmagnet.domain.model.SignInResult
import com.example.showmagnet.domain.model.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest

interface AuthRepository {
    suspend fun signUp(email: String, password: String): SignInResult

    suspend fun signIn(email: String, password: String): SignInResult

    suspend fun signInWithGoogle(): IntentSender?
    fun buildSignInRequest(): BeginSignInRequest

    suspend fun signInWithIntent(intent: Intent): SignInResult

    suspend fun signOut(): Boolean
    fun getSignedInUser(): UserData?
    fun isSignedIn(): Boolean
}