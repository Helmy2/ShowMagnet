package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SignInWithEmailUseCaseTest {
    private val repository = mock<UserRepository>()
    val useCase by lazy {
        SignInWithEmailUseCase(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successAndReturnTrue() = runBlocking {
        whenever(
            repository.signIn(
                email = "test@exampl.com", password = "password"
            )
        ).doReturn(Result.success(true))

        val result = useCase("test@exampl.com", "password")
        assert(result.isSuccess)
        assert(result.getOrNull() == true)
    }

    @Test
    fun successAndReturnFalse() = runBlocking {
        whenever(
            repository.signIn(
                email = "test@exampl.com", password = "password"
            )
        ).doReturn(Result.success(false))

        val result = useCase("test@exampl.com", "password")
        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun failureInSignIn() = runBlocking {
        whenever(
            repository.signIn(
                email = "test@exampl.com", password = "password"
            )
        ).doReturn(Result.failure(Exception()))

        val result = useCase("test@exampl.com", "password")
        assert(result.isFailure)
    }
}