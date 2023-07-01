package com.example.showmagnet.data.repository

import android.content.Context
import android.content.Intent
import com.example.showmagnet.R
import com.example.showmagnet.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient
) : AuthRepository {

    override suspend fun signUp(email: String, password: String) = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        Result.success(result.user != null)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override suspend fun signIn(email: String, password: String) = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        Result.success(result.user != null)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override suspend fun signInWithGoogle() = try {
        val result = oneTapClient.beginSignIn(
            buildSignInRequest()
        ).await()
        Result.success(result?.pendingIntent?.intentSender)
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
        Result.failure(e)
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    override suspend fun signInWithIntent(intent: Intent) = try {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        val user = auth.signInWithCredential(googleCredentials).await().user
        Result.success(user != null)
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
        Result.failure(e)
    }


    override suspend fun resetPassword(email: String) = try {
        auth.sendPasswordResetEmail(email).await()
        Result.success(true)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override fun isSignedIn() = try {
        Result.success(auth.currentUser != null)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}