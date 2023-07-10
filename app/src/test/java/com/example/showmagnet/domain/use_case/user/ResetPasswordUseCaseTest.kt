package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ResetPasswordUseCaseTest {
    private val repository = mock<UserRepository>()
    val useCase by lazy {
        ResetPasswordUseCase(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successAndReturnTrue() = runBlocking {
        whenever(repository.resetPassword("test@example.com")).doReturn(Result.success(true))

        val result = useCase("test@example.com")
        assert(result.isSuccess)
        assert(result.getOrNull() == true)
    }

    @Test
    fun successAndReturnFalse() = runBlocking {
        whenever(repository.resetPassword("test@example.com")).doReturn(Result.success(false))

        val result = useCase("test@example.com")
        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun failure() = runBlocking {
        whenever(repository.resetPassword("test@example.com")).doReturn(Result.failure(Exception()))

        val result = useCase("test@example.com")
        assert(result.isFailure)
    }
}