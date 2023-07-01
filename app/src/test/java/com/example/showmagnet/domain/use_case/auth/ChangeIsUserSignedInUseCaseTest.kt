package com.example.showmagnet.domain.use_case.auth

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ChangeIsUserSignedInUseCaseTest {
    private val userRepository = mock<UserRepository>()
    val useCase by lazy {
        ChangeIsUserSignedInUseCase(
            userRepository = userRepository
        )
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successAndReturnTrue() = runBlocking {
        whenever(userRepository.setIsSignedIn(true)).doReturn(Result.success(true))

        val result = useCase(true)
        assert(result.isSuccess)
        assert(result.getOrNull() == true)
    }

    @Test
    fun successAndReturnFalse() = runBlocking {
        whenever(userRepository.setIsSignedIn(true)).doReturn(Result.success(false))

        val result = useCase(true)
        assert(result.isSuccess)
        assert(result.getOrNull() == false)
    }

    @Test
    fun failure() = runBlocking {
        whenever(userRepository.setIsSignedIn(true)).doReturn(Result.failure(Exception()))

        val result = useCase(true)
        assert(result.isFailure)
    }
}