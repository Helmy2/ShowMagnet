package com.example.showmagnet.data.repository

import android.content.Intent
import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.data.source.preference.UserPreferencesManager
import com.example.showmagnet.domain.repository.UserRepository
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserRepositoryImplTest {
    private val auth: FirebaseAuth = mock()
    private val signInClient: SignInClient = mock()
    private val userPreferencesManager: UserPreferencesManager = mock()

    private lateinit var repository: UserRepository


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(
            auth,
            userPreferencesManager,
            signInClient,
            ""
        )
    }

    @Test
    fun updateProfileNameSuccessTest() = runBlocking {
        val result = repository.updateProfileName("name")
        assert(result.isSuccess)
    }

    @Test
    fun updateProfileNameFailureTest() = runBlocking {
        whenever(auth.currentUser).doThrow(NullPointerException())

        val result = repository.updateProfileName("name")
        assert(result.isFailure)
    }

    @Test
    fun signOutSuccessTest() = runBlocking {
        val result = repository.signOut()
        assert(result.isSuccess)
    }

    @Test
    fun signOutFailureTest() = runBlocking {
        whenever(auth.signOut()).doThrow(NullPointerException())

        val result = repository.signOut()
        assert(result.isFailure)
    }

    @Test
    fun getUserInfoFailureTest() = runBlocking {
        whenever(auth.currentUser).doReturn(null)

        val result = repository.getUserInfo()
        assert(result.isFailure)
    }

    @Test
    fun getUserInfoFailureWithExceptionTest() = runBlocking {
        whenever(auth.currentUser).doThrow(NullPointerException())

        val result = repository.getUserInfo()
        assert(result.isFailure)
    }

    @Test
    fun getUserInfoSuccessTest() = runBlocking {
        whenever(auth.currentUser).doReturn(mock())

        val result = repository.getUserInfo()
        assert(result.isSuccess)
    }

    @Test
    fun signUpFailureTest() = runBlocking {
        whenever(auth.createUserWithEmailAndPassword("email", "password")).doThrow(
            NullPointerException()
        )

        val result = repository.signUp(name = "name", email = "email", password = "password")

        assert(result.isFailure)
    }

    @Test
    fun signInFailureTest() = runBlocking {
        whenever(auth.signInWithEmailAndPassword("email", "password")).doThrow(
            NullPointerException()
        )

        val result = repository.signUp(name = "name", email = "email", password = "password")

        assert(result.isFailure)
    }

    @Test
    fun signInWithIntentFailureTest() = runBlocking {
        whenever(signInClient.getSignInCredentialFromIntent(Intent())).doThrow(
            NullPointerException()
        )

        val result = repository.signInWithIntent(Intent())

        assert(result.isFailure)
    }


    @Test
    fun resetPasswordFailureTest() = runBlocking {
        whenever(auth.sendPasswordResetEmail("email")).doThrow(NullPointerException())

        val result = repository.resetPassword("email")

        assert(result.isFailure)
    }


    @Test
    fun isSignedInSuccessTrueTest() = runBlocking {
        whenever(auth.currentUser).doReturn(mock())

        val result = repository.isSignedIn()

        assert(result.isSuccess)
        assert(result.getOrNull() == true)
    }

    @Test
    fun isSignedInSuccessFalseTest() = runBlocking {
        whenever(auth.currentUser).doReturn(null)

        val result = repository.isSignedIn()

        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun isSignedInFailureFalseTest() = runBlocking {
        whenever(auth.currentUser).doThrow(NullPointerException())

        val result = repository.isSignedIn()

        assert(result.isFailure)
    }

}