package com.example.showmagnet.ui.auth.sign_up

import com.example.showmagnet.MainDispatcherRule
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
    fun isValuedEmpty() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.NameChanged(""))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged(""))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged(""))
        val state = viewModel.viewState.value

        assert(!state.isValuedSignUp)
        assert(state.name == "")
        assert(state.email == "")
        assert(state.password == "")
    }

    @Test
    fun isValuedEmptySomeFeild() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.NameChanged("name"))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged(""))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged(""))
        val state = viewModel.viewState.value

        assert(!state.isValuedSignUp)


        viewModel.handleEvents(SignUpContract.Event.NameChanged(""))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test@example.com"))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged(""))
        assert(!state.isValuedSignUp)

        viewModel.handleEvents(SignUpContract.Event.NameChanged(""))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged(""))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("password"))

        assert(!state.isValuedSignUp)
    }

    @Test
    fun isValuedPasswordCase1() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.NameChanged("name"))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test@example.com"))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("absd"))
        val state = viewModel.viewState.value

        assert(!state.isValuedSignUp)
        assert(state.passwordRequirement == "Password must be at least 8 characters")
    }

    @Test
    fun isValuedPasswordCase2() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.NameChanged("name"))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test@example.com"))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("absdcdefcd"))
        val state = viewModel.viewState.value

        assert(!state.isValuedSignUp)
        assert(state.passwordRequirement == "Password must contain at least one uppercase letter")
    }

    @Test
    fun isValuedPasswordCase3() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.NameChanged("name"))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test@example.com"))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("Absdcdefcd"))
        val state = viewModel.viewState.value

        assert(!state.isValuedSignUp)
        assert(state.passwordRequirement == "Password must contain at least one digit")
    }

    @Test
    fun isValuedPasswordCase4() = runBlocking {
        viewModel.handleEvents(SignUpContract.Event.NameChanged("name"))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test@example.com"))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("Absdcdefcd1"))
        val state = viewModel.viewState.value

        assert(state.isValuedSignUp)
        assert(state.passwordRequirement == null)
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
            .doReturn(Result.failure(Exception("Error")))

        viewModel.handleEvents(SignUpContract.Event.NameChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.SignUP)

        val state = viewModel.viewState.value
        val result = viewModel.effect.first()

        if (state.loading)
            isLoadingRequest = true

        assert(!state.isValuedSignUp)
        assert(isLoadingRequest)
        assert(result is SignUpContract.Effect.ShowErrorToast)
        assert((result as SignUpContract.Effect.ShowErrorToast).message == "Error")
    }

    @Test
    fun signUpSuccessesEvent() = runBlocking {
        var isLoadingRequest = false
        whenever(signUpUseCaseMock("test", "test", "Password123"))
            .doReturn(Result.success(true))

        viewModel.handleEvents(SignUpContract.Event.NameChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.EmailChanged("test"))
        viewModel.handleEvents(SignUpContract.Event.PasswordChanged("Password123"))
        viewModel.handleEvents(SignUpContract.Event.SignUP)

        val state = viewModel.viewState.value
        val result = viewModel.effect.first()

        if (state.loading)
            isLoadingRequest = true

        assert(state.isValuedSignUp)
        assert(isLoadingRequest)
        assert(result is SignUpContract.Effect.ShowSuccessToast)
    }
}

