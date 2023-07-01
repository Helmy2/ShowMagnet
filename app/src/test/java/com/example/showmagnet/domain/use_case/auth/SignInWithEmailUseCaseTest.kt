package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SignInWithEmailUseCaseTest {
    private val authRepository = mock<AuthRepository>()
    private val changeIsUserSignedInUseCase = mock<ChangeIsUserSignedInUseCase>()
    val useCase by lazy {
        SignInWithEmailUseCase(
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
            authRepository.signIn(
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.success(true))

        val result = useCase("test@exampl.com", "password")
        assert(result.isSuccess)
        assert(result.getOrNull() == true)
    }

    @Test
    fun successAndReturnFalse() = runBlocking {
        whenever(changeIsUserSignedInUseCase(true)).doReturn(Result.success(false))
        whenever(
            authRepository.signIn(
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.success(true))

        val result = useCase("test@exampl.com", "password")
        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun failureInSignIn() = runBlocking {
        whenever(changeIsUserSignedInUseCase(true)).doReturn(Result.success(true))
        whenever(
            authRepository.signIn(
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.failure(Exception()))

        val result = useCase("test@exampl.com", "password")
        assert(result.isFailure)
    }

    @Test
    fun failureInChangeIsSignedIn() = runBlocking {
        whenever(changeIsUserSignedInUseCase(true)).doReturn(Result.failure(Exception()))
        whenever(
            authRepository.signIn(
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.success(true))

        val result = useCase("test@exampl.com", "password")
        assert(result.isFailure)
    }
}