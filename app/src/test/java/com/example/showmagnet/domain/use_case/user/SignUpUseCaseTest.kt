package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SignUpUseCaseTest {
    private val repository = mock<UserRepository>()
    val useCase by lazy {
        SignUpUseCase(
            repository
        )
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successAndReturnTrue() = runBlocking {
        whenever(
            repository.signUp(
                name = "name",
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.success(true))

        val result = useCase("name", "test@exampl.com", "password")
        assert(result.isSuccess)
        assert(result.getOrNull() == true)
    }

    @Test
    fun successAndReturnFalse() = runBlocking {
        whenever(
            repository.signUp(
                name = "name",
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.success(false))

        val result = useCase("name", "test@exampl.com", "password")

        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun failureInSignUp() = runBlocking {
        whenever(
            repository.signUp(
                name = "name",
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.failure(Exception()))

        val result = useCase("name", "test@exampl.com", "password")


        assert(result.isFailure)
    }

    @Test
    fun failureInChangeName() = runBlocking {
        whenever(
            repository.signUp(
                name = "name",
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.failure(Exception()))

        val result = useCase("name", "test@exampl.com", "password")


        assert(result.isFailure)
    }
}