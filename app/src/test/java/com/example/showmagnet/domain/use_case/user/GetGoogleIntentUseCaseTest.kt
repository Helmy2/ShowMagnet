package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetGoogleIntentUseCaseTest {
    private val repository = mock<UserRepository>()
    val useCase by lazy {
        GetGoogleIntentUseCase(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun success() = runBlocking {
        whenever(repository.signInWithGoogle()).doReturn(Result.success(null))

        val result = useCase()
        assert(result.isSuccess)
    }


    @Test
    fun failure() = runBlocking {
        whenever(repository.signInWithGoogle()).doReturn(Result.failure(Exception()))

        val result = useCase()
        assert(result.isFailure)
    }
}
