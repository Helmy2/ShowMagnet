package com.example.showmagnet.ui.auth.sign_in

import com.example.showmagnet.MainDispatcherRule
import com.example.showmagnet.domain.use_case.auth.GetGoogleIntentUseCase
import com.example.showmagnet.domain.use_case.auth.ResetPasswordUseCase
import com.example.showmagnet.domain.use_case.auth.SignInWithEmailUseCase
import com.example.showmagnet.domain.use_case.auth.SignInWithGoogleUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SignInViewModelTest {
    val signInWithEmailUseCase = mock<SignInWithEmailUseCase>()
    val signInWithGoogleUseCase = mock<SignInWithGoogleUseCase>()
    val resetPasswordUseCase = mock<ResetPasswordUseCase>()
    val getGoogleIntentUseCase = mock<GetGoogleIntentUseCase>()

    private val viewModel: SignInViewModel by lazy {
        SignInViewModel(
            signInWithEmailUseCase = signInWithEmailUseCase,
            signInWithGoogleUseCase = signInWithGoogleUseCase,
            resetPasswordUseCase = resetPasswordUseCase,
            getGoogleIntentUseCase = getGoogleIntentUseCase,
        )
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun passwordChangeEvent() = runBlocking {
        viewModel.handleEvents(SignInContract.Event.PasswordChanged("password"))

        val state = viewModel.viewState.value

        assert(state.password == "password")
    }

    @Test
    fun emailChangeEvent() = runBlocking {
        viewModel.handleEvents(SignInContract.Event.EmailChanged("test@example.com"))

        val state = viewModel.viewState.value

        assert(state.email == "test@example.com")
    }

    @Test
    fun navigateToSignUpEvent() = runBlocking {
        viewModel.handleEvents(SignInContract.Event.Navigation.ToSignUp)

        val result = viewModel.effect.first()

        assert(result is SignInContract.Effect.Navigation.ToSignUp)
    }

    @Test
    fun startSignInWithGoogleEvent() = runBlocking {
        viewModel.handleEvents(SignInContract.Event.StartSignInWithGoogle)

        val result = viewModel.effect.first()

        assert(result is SignInContract.Effect.StartSignInWithGoogle)
    }

    @Test
    fun signUpWithEmailFailedEvent() = runBlocking {
        var isLoadingRequest = false
        whenever(signInWithEmailUseCase("test", "test"))
            .doReturn(Result.failure(Exception("Error")))

        viewModel.handleEvents(SignInContract.Event.EmailChanged("test"))
        viewModel.handleEvents(SignInContract.Event.PasswordChanged("test"))
        viewModel.handleEvents(SignInContract.Event.SignInWithEmail)

        val state = viewModel.viewState.value
        val result = viewModel.effect.first()

        if (state.loadingWithEmail)
            isLoadingRequest = true

        assert(isLoadingRequest)
        assert(result is SignInContract.Effect.ShowErrorToast)
        assert((result as SignInContract.Effect.ShowErrorToast).message == "Error")
    }

    @Test
    fun resetPasswordFailedEvent() = runBlocking {
        whenever(resetPasswordUseCase("test@example.com"))
            .doReturn(Result.failure(Exception("Error")))

        viewModel.handleEvents(SignInContract.Event.ResetPassword("test@example.com"))

        val result = viewModel.effect.first()

        assert(result is SignInContract.Effect.ShowErrorToast)
        assert((result as SignInContract.Effect.ShowErrorToast).message == "Error")
    }

    @Test
    fun resetPasswordSuccessEvent() = runBlocking {
        whenever(resetPasswordUseCase("test@example.com"))
            .doReturn(Result.success(true))

        viewModel.handleEvents(SignInContract.Event.ResetPassword("test@example.com"))

        val result = viewModel.effect.first()

        assert(result is SignInContract.Effect.ShowSuccessToast)
    }


}