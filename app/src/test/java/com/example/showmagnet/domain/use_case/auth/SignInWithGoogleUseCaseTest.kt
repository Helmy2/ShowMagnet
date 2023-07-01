package com.example.showmagnet.domain.use_case.auth

import android.content.Intent
import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SignInWithGoogleUseCaseTest {
    private val authRepository = mock<AuthRepository>()
    private val changeIsUserSignedInUseCase = mock<ChangeIsUserSignedInUseCase>()
    val useCase by lazy {
        SignInWithGoogleUseCase(
            authRepository,
            changeIsUserSignedInUseCase
        )
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successAndReturnTrue() = runBlocking {
        whenever(changeIsUserSignedInUseCase(true)).doReturn(Result.success(true))
        whenever(
            authRepository.signInWithIntent(Intent())
        ).doReturn(Result.success(true))

        val result = useCase(Intent())
        assert(result.isSuccess)
        assert(result.getOrNull() == true)
    }

    @Test
    fun successAndReturnFalse() = runBlocking {
        whenever(changeIsUserSignedInUseCase(true)).doReturn(Result.success(false))
        whenever(
            authRepository.signInWithIntent(Intent())
        ).doReturn(Result.success(true))

        val result = useCase(Intent())
        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun failureInChangeIsSignedIn() = runBlocking {
        whenever(changeIsUserSignedInUseCase(true)).doReturn(Result.failure(Exception()))
        whenever(
            authRepository.signInWithIntent(Intent())
        ).doReturn(Result.success(true))

        val result = useCase(Intent())
        assert(result.isFailure)
    }
}