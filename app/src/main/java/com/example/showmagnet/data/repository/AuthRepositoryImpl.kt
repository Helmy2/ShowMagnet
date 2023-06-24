package com.example.showmagnet.data.repository

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.showmagnet.R
import com.example.showmagnet.domain.model.SignInResult
import com.example.showmagnet.domain.model.UserData
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

    override suspend fun signUp(email: String, password: String): SignInResult {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await()
            SignInResult(user == null)
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(false)
        }
    }

    override suspend fun signIn(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await()
            SignInResult(user == null)
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(false)
        }
    }

    override suspend fun signInWithGoogle(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override fun buildSignInRequest(): BeginSignInRequest {
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

    override suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(user != null)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(false, e.localizedMessage?.toString())
        }
    }

    override suspend fun signOut(): Boolean {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
        return false
    }

    override fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }
}