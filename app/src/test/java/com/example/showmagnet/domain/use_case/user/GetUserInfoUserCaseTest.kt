package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.model.UserData
import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetUserInfoUserCaseTest {
    private val userRepository = mock<UserRepository>()
    val useCase by lazy {
        GetUserInfoUserCase(
            userRepository
        )
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun success() = runBlocking {
        whenever(userRepository.getUserInfo()).doReturn(
            Result.success(
                UserData(
                    "test",
                    "test",
                    "test"
                )
            )
        )
        val result = useCase()
        assert(result.isSuccess)
        assert(result.getOrNull() == UserData("test", "test", "test"))
    }

    @Test
    fun failureInChangeIsSignedIn() = runBlocking {
        whenever(userRepository.getUserInfo()).doReturn(
            Result.failure(Exception())
        )

        val result = useCase()
        assert(result.isFailure)
    }
}