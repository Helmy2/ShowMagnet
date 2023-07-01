package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ResetPasswordUseCaseTest {
    private val authRepository = mock<AuthRepository>()
    val useCase by lazy {
        ResetPasswordUseCase(
            authRepository = authRepository
        )
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successAndReturnTrue() = runBlocking {
        whenever(authRepository.resetPassword("test@example.com")).doReturn(Result.success(true))

        val result = useCase("test@example.com")
        assert(result.isSuccess)
        assert(result.getOrNull() == true)
    }

    @Test
    fun successAndReturnFalse() = runBlocking {
        whenever(authRepository.resetPassword("test@example.com")).doReturn(Result.success(false))

        val result = useCase("test@example.com")
        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun failure() = runBlocking {
        whenever(authRepository.resetPassword("test@example.com")).doReturn(Result.failure(Exception()))

        val result = useCase("test@example.com")
        assert(result.isFailure)
    }
}