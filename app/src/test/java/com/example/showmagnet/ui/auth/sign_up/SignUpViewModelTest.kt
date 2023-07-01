package com.example.showmagnet.ui.auth.sign_up

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.model.SignResult
import com.example.showmagnet.domain.use_case.auth.SignUpUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SignUpViewModelTest {
    val signUpUseCaseMock = mock<SignUpUseCase>()

    private val viewModel: SignUpViewModel by lazy { SignUpViewModel(signUpUseCaseMock) }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun nameChangeEvent() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.NameChanged("name"))

        val state = viewModel.viewState.value

        assert(state.name == "name")
    }

    @Test
    fun passwordChangeEvent() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("password"))

        val state = viewModel.viewState.value

        assert(state.password == "password")
    }

    @Test
    fun emailChangeEvent() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test@example.com"))

        val state = viewModel.viewState.value

        assert(state.email == "test@example.com")
    }


    @Test
    fun navigateToSignInEvent() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.Navigation.ToSignIn)

        val result = viewModel.effect.first()

        assert(result is SignUpContract.Effect.Navigation.ToSignIn)
    }


    @Test
    fun signUpFailedEvent() = runBlocking {
        var isLoadingRequest = false
        whenever(signUpUseCaseMock("test", "test", "test"))
            .doReturn(SignResult(success = false, errorMessage = "Error"))

        viewModel.handleEvents(SignUpContract.Event.NameChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.SignUP)

        val state = viewModel.viewState.value
        val result = viewModel.effect.first()

        if (state.loading)
            isLoadingRequest = true

        assert(isLoadingRequest)
        assert(result is SignUpContract.Effect.ShowErrorToast)
        assert((result as SignUpContract.Effect.ShowErrorToast).message == "Error")
    }

    @Test
    fun signUpSuccessesEvent() = runBlocking {
        var isLoadingRequest = false
        whenever(signUpUseCaseMock("test", "test", "test"))
            .doReturn(SignResult(success = true))

        viewModel.handleEvents(SignUpContract.Event.NameChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.SignUP)

        val state = viewModel.viewState.value
        val result = viewModel.effect.first()

        if (state.loading)
            isLoadingRequest = true

        assert(isLoadingRequest)
        assert(result is SignUpContract.Effect.ShowSuccessToast)
    }
}

