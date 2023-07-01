package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetGoogleIntentUseCaseTest {
    private val authRepository = mock<AuthRepository>()
    val useCase by lazy {
        GetGoogleIntentUseCase(authRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun success() = runBlocking {
        whenever(authRepository.signInWithGoogle()).doReturn(Result.success(null))

        val result = useCase()
        assert(result.isSuccess)
    }


    @Test
    fun failure() = runBlocking {
        whenever(authRepository.signInWithGoogle()).doReturn(Result.failure(Exception()))

        val result = useCase()
        assert(result.isFailure)
    }
}
