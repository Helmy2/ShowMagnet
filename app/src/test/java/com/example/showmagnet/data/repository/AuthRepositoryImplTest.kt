package com.example.showmagnet.data.repository

import android.content.Intent
import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.AuthRepository
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

class AuthRepositoryImplTest {
    private val auth: FirebaseAuth = mock()
    private val oneTapClient: SignInClient = mock()
    private lateinit var authRepository: AuthRepository


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl(mock(), auth, oneTapClient)
    }

    @Test
    fun signUpFailureTest() = runBlocking {
        whenever(auth.createUserWithEmailAndPassword("email", "password")).doThrow(
            NullPointerException()
        )

        val result = authRepository.signUp("email", "password")

        assert(result.isFailure)
    }

    @Test
    fun signInFailureTest() = runBlocking {
        whenever(auth.signInWithEmailAndPassword("email", "password")).doThrow(
            NullPointerException()
        )

        val result = authRepository.signUp("email", "password")

        assert(result.isFailure)
    }

    @Test
    fun signInWithIntentFailureTest() = runBlocking {
        whenever(oneTapClient.getSignInCredentialFromIntent(Intent())).doThrow(
            NullPointerException()
        )

        val result = authRepository.signInWithIntent(Intent())

        assert(result.isFailure)
    }


    @Test
    fun resetPasswordFailureTest() = runBlocking {
        whenever(auth.sendPasswordResetEmail("email")).doThrow(NullPointerException())

        val result = authRepository.resetPassword("email")

        assert(result.isFailure)
    }


    @Test
    fun isSignedInSuccessTrueTest() = runBlocking {
        whenever(auth.currentUser).doReturn(mock())

        val result = authRepository.isSignedIn()

        assert(result.isSuccess)
        assert(result.getOrNull() == true)
    }

    @Test
    fun isSignedInSuccessFalseTest() = runBlocking {
        whenever(auth.currentUser).doReturn(null)

        val result = authRepository.isSignedIn()

        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun isSignedInFailureFalseTest() = runBlocking {
        whenever(auth.currentUser).doThrow(NullPointerException())

        val result = authRepository.isSignedIn()

        assert(result.isFailure)
    }
}