package com.example.showmagnet.data.repository

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.data.source.preference.UserPreferencesManager
import com.example.showmagnet.domain.repository.UserRepository
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
    private val userPreferencesManager: UserPreferencesManager = mock()

    private lateinit var userRepository: UserRepository


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        userRepository = UserRepositoryImpl(auth, userPreferencesManager)
    }

    @Test
    fun updateProfileNameSuccessTest() = runBlocking {
        val result = userRepository.updateProfileName("name")
        assert(result.isSuccess)
    }

    @Test
    fun updateProfileNameFailureTest() = runBlocking {
        whenever(auth.currentUser).doThrow(NullPointerException())

        val result = userRepository.updateProfileName("name")
        assert(result.isFailure)
    }

    @Test
    fun signOutSuccessTest() = runBlocking {
        val result = userRepository.signOut()
        assert(result.isSuccess)
    }

    @Test
    fun signOutFailureTest() = runBlocking {
        whenever(auth.signOut()).doThrow(NullPointerException())

        val result = userRepository.signOut()
        assert(result.isFailure)
    }

    @Test
    fun getUserInfoFailureTest() = runBlocking {
        whenever(auth.currentUser).doReturn(null)

        val result = userRepository.getUserInfo()
        assert(result.isFailure)
    }

    @Test
    fun getUserInfoFailureWithExceptionTest() = runBlocking {
        whenever(auth.currentUser).doThrow(NullPointerException())

        val result = userRepository.getUserInfo()
        assert(result.isFailure)
    }

    @Test
    fun getUserInfoSuccessTest() = runBlocking {
        whenever(auth.currentUser).doReturn(mock())

        val result = userRepository.getUserInfo()
        assert(result.isSuccess)
    }

    @Test
    fun setIsSignedInSuccessTest() = runBlocking {
        whenever(
            userPreferencesManager.updateIsUserSignedIn(true)
        ).doReturn(Result.success(true))

        val result = userRepository.setIsSignedIn(true)
        assert(result.isSuccess)
    }

    @Test
    fun setIsSignedInFailureTest() = runBlocking {
        whenever(
            userPreferencesManager.updateIsUserSignedIn(true)
        ).doReturn(Result.failure(Exception()))

        val result = userRepository.setIsSignedIn(true)
        assert(result.isFailure)
    }


}