package com.example.showmagnet.domain.use_case.user

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.model.UserPreferences
import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class IsUserSingedInUserCaseTest {
    private val userRepository = mock<UserRepository>()
    val useCase by lazy {
        IsUserSingedInUserCase(
            userRepository
        )
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun userSignedIn() = runBlocking {
        whenever(userRepository.userPreferencesFlow).doReturn(flow {
            emit(
                UserPreferences(
                    isUserSignedIn = true
                )
            )
        })

        val result = useCase().last()
        assert(result)
    }

    @Test
    fun userNotSignedIn() = runBlocking {
        whenever(userRepository.userPreferencesFlow).doReturn(flow {
            emit(
                UserPreferences(
                    isUserSignedIn = false
                )
            )
        })

        val result = useCase().last()
        assert(!result)
    }
}