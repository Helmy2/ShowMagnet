package com.example.showmagnet.ui.auth.sign_in

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.ResetPasswordUseCase
import com.example.showmagnet.domain.use_case.SignInWithEmailUseCase
import com.example.showmagnet.domain.use_case.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : ViewModel() {
    val uiState = MutableStateFlow(SignInState())


    init {
        viewModelScope.launch {
            val intentSender = signInWithGoogleUseCase.getIntentSender()

            withContext(Dispatchers.Main) {
                uiState.value = uiState.value.copy(
                    intentSender = intentSender
                )
            }
        }
    }

    private fun updateEmail(email: String) {
        this.uiState.value = this.uiState.value.copy(
            email = email
        )
    }

    private fun updatePassword(password: String) {
        this.uiState.value = this.uiState.value.copy(
            password = password,
        )
    }

    private fun dismissError() {
        uiState.value = uiState.value.copy(
            error = null
        )
    }

    private fun signIn() {
        uiState.value = uiState.value.copy(
            loading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                signInWithEmailUseCase(uiState.value.email!!, uiState.value.password!!)

            withContext(Dispatchers.Main) {
                if (result.success)
                    uiState.value = uiState.value.copy(loading = false, successSignIn = true)
                else
                    uiState.value = uiState.value.copy(
                        loading = false,
                        error = result.errorMessage
                    )
            }
        }
    }

    private fun navigateToHome() {
        uiState.value = uiState.value.copy(
            navigateTo = SignInState.NavigateTo.Home,
            successSignIn = false
        )
    }

    private fun navigateToSignUP() {
        uiState.value = uiState.value.copy(
            navigateTo = SignInState.NavigateTo.SIGN_UP,
        )
    }

    private fun dismissNavigation() {
        uiState.value = uiState.value.copy(
            navigateTo = SignInState.NavigateTo.None,
        )
    }

    private fun signInWithGoogle(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = signInWithGoogleUseCase(intent)
            if (result.success)
                uiState.value = uiState.value.copy(loading = false, successSignIn = true)
            else
                uiState.value = uiState.value.copy(
                    loading = false,
                    error = result.errorMessage
                )
        }
    }

    private fun startSignInWithGoogle() {
        uiState.value = uiState.value.copy(
            startSignInWithGoogle = true
        )
    }

    private fun resetPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = resetPasswordUseCase(email)
            if (result.success)
                uiState.value = uiState.value.copy(
                    loading = false,
                    success = "Password reset email sent to $email"
                )
            else
                uiState.value = uiState.value.copy(
                    loading = false,
                    error = result.errorMessage
                )
        }
    }

    fun handleEvent(signInEvent: SignInEvent) {
        when (signInEvent) {
            is SignInEvent.EmailChanged -> updateEmail(signInEvent.email)
            is SignInEvent.PasswordChanged -> updatePassword(signInEvent.password)
            is SignInEvent.SignInWithGoogle -> signInWithGoogle(signInEvent.intent)
            is SignInEvent.ResetPassword -> resetPassword(signInEvent.email)

            SignInEvent.SignInWithEmail -> signIn()
            SignInEvent.ErrorDismissed -> dismissError()
            SignInEvent.NavigateToSignUp -> navigateToSignUP()
            SignInEvent.NavigateToHome -> navigateToHome()
            SignInEvent.NavigateDismissed -> dismissNavigation()
            SignInEvent.StartSignInWithGoogle -> startSignInWithGoogle()
        }
    }
}