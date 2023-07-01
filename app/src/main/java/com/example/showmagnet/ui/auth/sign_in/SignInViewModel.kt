package com.example.showmagnet.ui.auth.sign_in

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.auth.GetGoogleIntentUseCase
import com.example.showmagnet.domain.use_case.auth.ResetPasswordUseCase
import com.example.showmagnet.domain.use_case.auth.SignInWithEmailUseCase
import com.example.showmagnet.domain.use_case.auth.SignInWithGoogleUseCase
import com.example.showmagnet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val getGoogleIntentUseCase: GetGoogleIntentUseCase
) : BaseViewModel<SignInContract.Event, SignInContract.State, SignInContract.Effect>() {

    override fun setInitialState() = SignInContract.State()

    override fun handleEvents(event: SignInContract.Event) {
        when (event) {
            is SignInContract.Event.EmailChanged -> updateEmail(event.email)
            is SignInContract.Event.PasswordChanged -> updatePassword(event.password)
            is SignInContract.Event.ResetPassword -> resetPassword(event.email)
            is SignInContract.Event.SignInWithGoogle -> signInWithGoogle(event.intent)
            SignInContract.Event.SignInWithEmail -> signIn()
            SignInContract.Event.StartSignInWithGoogle -> startSignInWithGoogle()
            SignInContract.Event.Navigation.ToSignUp -> navigateToSignUp()
        }
    }

    private fun updateEmail(email: String) {
        setState { copy(email = email) }
    }

    private fun updatePassword(password: String) {
        setState { copy(password = password) }
    }

    private fun signIn() {
        setState { copy(loadingWithEmail = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                signInWithEmailUseCase(viewState.value.email, viewState.value.password)

            withContext(Dispatchers.Main) {
                if (!result.success)
                    setEffect { SignInContract.Effect.ShowErrorToast(result.errorMessage ?: "") }
                setState { copy(loadingWithEmail = false) }
            }
        }
    }

    private fun signInWithGoogle(intent: Intent) {
        setState { copy(loadingWithGoogle = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val result = signInWithGoogleUseCase(intent)

            withContext(Dispatchers.Main) {
                if (!result.success)
                    setEffect { SignInContract.Effect.ShowErrorToast(result.errorMessage ?: "") }
                setState { copy(loadingWithGoogle = false) }
            }
        }
    }

    private fun navigateToSignUp() {
        setEffect { SignInContract.Effect.Navigation.ToSignUp }
    }

    private fun startSignInWithGoogle() {
        viewModelScope.launch(Dispatchers.IO) {
            val intentSender = getGoogleIntentUseCase()

            withContext(Dispatchers.Main) {
                setEffect {
                    SignInContract.Effect.StartSignInWithGoogle(intentSender)
                }
            }
        }
    }

    private fun resetPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = resetPasswordUseCase(email)
            if (result.success)
                setEffect { SignInContract.Effect.ShowSuccessToast("Password reset email sent to $email") }
            else
                setEffect { SignInContract.Effect.ShowErrorToast(result.errorMessage ?: "") }
        }
    }
}