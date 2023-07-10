package com.example.showmagnet.domain.use_case.user

import android.content.Intent
import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SignInWithGoogleUseCaseTest {
    private val repository = mock<UserRepository>()
    val useCase by lazy {
        SignInWithGoogleUseCase(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successAndReturnTrue() = runBlocking {
        whenever(
            repository.signInWithIntent(Intent())
        ).doReturn(Result.success(true))

        val result = useCase(Intent())
        assert(result.isSuccess)
    }

    @Test
    fun successAndReturnFalse() = runBlocking {
        whenever(
            repository.signInWithIntent(Intent())
        ).doReturn(Result.success(false))

        val result = useCase(Intent())
        assert(result.isSuccess)
    }
}