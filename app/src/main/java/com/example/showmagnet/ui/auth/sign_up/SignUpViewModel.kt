package com.example.showmagnet.ui.auth.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    val uiState = MutableStateFlow(SignUpState())

    private fun updateName(name: String) {
        this.uiState.value = this.uiState.value.copy(
            name = name
        )
    }

    private fun updateEmail(email: String) {
        this.uiState.value = this.uiState.value.copy(
            email = email
        )
    }

    private fun updatePassword(password: String) {
        val requirements = mutableListOf<PasswordRequirement>()
        if (password.length < 7) {
            requirements.add(PasswordRequirement.EIGHT_CHARACTERS)
        }
        if (password.none { it.isUpperCase() }) {
            requirements.add(PasswordRequirement.CAPITAL_LETTER)
        }
        if (password.none { it.isDigit() }) {
            requirements.add(PasswordRequirement.NUMBER)
        }
        this.uiState.value = this.uiState.value.copy(
            password = password,
            passwordRequirements = requirements.toList()
        )
    }

    private fun dismissError() {
        uiState.value = uiState.value.copy(
            error = null
        )
    }

    private fun signUp() {
        uiState.value = uiState.value.copy(
            loading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                signUpUseCase(uiState.value.name!!, uiState.value.email!!, uiState.value.password!!)
            withContext(Dispatchers.Main) {
                if (result.success)
                    uiState.value = uiState.value.copy(loading = false, successSignUp = true)
                else
                    uiState.value = uiState.value.copy(
                        loading = false,
                        error = result.errorMessage
                    )
            }
        }
    }

    private fun navigate() {
        uiState.value = uiState.value.copy(
            successSignUp = false,
            navigateToNextScreen = true,
        )
    }

    private fun dismissNavigation() {
        uiState.value = uiState.value.copy(
            navigateToNextScreen = false
        )
    }

    fun handleEvent(signUpEvent: SignUpEvent) {
        when (signUpEvent) {
            is SignUpEvent.EmailChanged -> updateEmail(signUpEvent.email)
            is SignUpEvent.NameChanged -> updateName(signUpEvent.name)
            is SignUpEvent.PasswordChanged -> updatePassword(signUpEvent.password)

            SignUpEvent.SignUP -> signUp()
            SignUpEvent.ErrorDismissed -> dismissError()
            SignUpEvent.NavigateToSignIn -> navigate()
            SignUpEvent.NavigateDismissed -> dismissNavigation()
        }
    }
}