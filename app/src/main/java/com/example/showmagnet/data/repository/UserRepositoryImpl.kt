package com.example.showmagnet.data.repository

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val userPreferencesManager: UserPreferencesManager,
    private val signInClient: SignInClient,
    @DefaultWebClientId private val webClientId: String,
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
        else {
            Result.success(
                UserData(
                    email = user.email ?: "",
                    username = user.displayName ?: "",
                    url = user.photoUrl.toString()
                )
            )
        }
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

    override suspend fun updateDarkTheme(darkTheme: Boolean) = try {
        userPreferencesManager.updateDarkTheme(darkTheme)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun updateProfile(name: String, bitmap: Bitmap?): Result<Boolean> {
        updateProfileName(name)
        if (bitmap != null)
            uploadProfileImage(bitmap)

        return Result.success(true)
    }

    private suspend fun uploadProfileImage(bitmap: Bitmap) {
        val user = auth.currentUser ?: throw Exception("User not found")

        val riversRef = storage.reference.child("images/${user.uid}.jpg")

        // compressing image
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        val reducedImage = byteArrayOutputStream.toByteArray()

        val uploadTask = riversRef.putBytes(reducedImage)
        val uri = uploadTask.continueWith { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            riversRef.downloadUrl
        }.await().await()

        updateProfileImage(uri)
    }

    private suspend fun updateProfileImage(uri: Uri?) {
        val user = auth.currentUser ?: throw Exception("User not found")
        user.updateProfile(userProfileChangeRequest { photoUri = uri }).await()
    }
}