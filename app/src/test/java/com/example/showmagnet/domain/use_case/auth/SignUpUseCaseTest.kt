package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.AuthRepository
import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SignUpUseCaseTest {
    private val authRepository = mock<AuthRepository>()
    private val userRepository = mock<UserRepository>()
    val useCase by lazy {
        SignUpUseCase(
            authRepository,
            userRepository
        )
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successAndReturnTrue() = runBlocking {
        whenever(userRepository.updateProfileName("name")).doReturn(Result.success(true))
        whenever(
            authRepository.signUp(
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
        whenever(userRepository.updateProfileName("name")).doReturn(Result.success(false))
        whenever(
            authRepository.signUp(
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.success(true))

        val result = useCase("name", "test@exampl.com", "password")

        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun failureInSignUp() = runBlocking {
        whenever(userRepository.updateProfileName("name")).doReturn(Result.success(true))
        whenever(
            authRepository.signUp(
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.failure(Exception()))

        val result = useCase("name", "test@exampl.com", "password")


        assert(result.isFailure)
    }

    @Test
    fun failureInChangeName() = runBlocking {
        whenever(userRepository.updateProfileName("name")).doReturn(Result.failure(Exception()))
        whenever(
            authRepository.signUp(
                email = "test@exampl.com",
                password = "password"
            )
        ).doReturn(Result.success(true))

        val result = useCase("name", "test@exampl.com", "password")


        assert(result.isFailure)
    }
}