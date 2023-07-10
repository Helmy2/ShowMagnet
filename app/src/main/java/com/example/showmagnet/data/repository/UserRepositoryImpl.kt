package com.example.showmagnet.data.repository

import android.content.Intent
import com.example.showmagnet.data.source.preference.UserPreferencesManager
import com.example.showmagnet.di.DefaultWebClientId
import com.example.showmagnet.domain.model.user.UserData
import com.example.showmagnet.domain.model.user.UserPreferences
import com.example.showmagnet.domain.repository.UserRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userPreferencesManager: UserPreferencesManager,
    private val signInClient: SignInClient,
    @DefaultWebClientId private val webClientId: String
) : UserRepository {

    override val userPreferencesFlow: Flow<UserPreferences> =
        userPreferencesManager.userPreferencesFlow

    override suspend fun updateProfileName(name: String) = try {
        val user = auth.currentUser
        user?.updateProfile(userProfileChangeRequest { displayName = name })?.await()

        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserInfo(): Result<UserData> = try {
        val user = auth.currentUser
        if (user == null) Result.failure(Exception("User not found"))
        else Result.success(
            UserData(
                email = user.email ?: "",
                username = user.displayName ?: "",
                profilePictureUrl = user.photoUrl?.toString()
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun signUp(name: String, email: String, password: String) = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        if (result.user != null)
            updateProfileName(name)

        Result.success(result.user != null)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }


    override suspend fun signIn(email: String, password: String) = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()

        userPreferencesManager.updateIsUserSignedIn(result.user != null)
        Result.success(result.user != null)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun signOut() = try {
        auth.signOut()
        userPreferencesManager.updateIsUserSignedIn(false)
        Result.success(true)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun signInWithGoogle() = try {
        val result = signInClient.beginSignIn(
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
                    .setServerClientId(webClientId)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    override suspend fun signInWithIntent(intent: Intent) = try {
        val credential = signInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        val user = auth.signInWithCredential(googleCredentials).await().user
        userPreferencesManager.updateIsUserSignedIn(user != null)
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