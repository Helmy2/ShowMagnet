package com.example.showmagnet.ui.auth.sign_up

import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.SignUpUseCase
import com.example.showmagnet.ui.auth.sign_up.SignUpContract.Effect
import com.example.showmagnet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel<SignUpContract.Event, SignUpContract.State, Effect>() {

    override fun setInitialState() = SignUpContract.State()

    override fun handleEvents(event: SignUpContract.Event) {
        when (event) {

            is SignUpContract.Event.NameChanged -> updateName(event.name)
            is SignUpContract.Event.EmailChanged -> updateEmail(event.email)
            is SignUpContract.Event.PasswordChanged -> updatePassword(event.password)

            SignUpContract.Event.SignUP -> signUp()
            SignUpContract.Event.Navigation.ToSignIn -> navigate()
        }
    }


    private fun updateName(name: String) {
        setState { copy(name = name) }
    }

    private fun updateEmail(email: String) {
        setState { copy(email = email) }
    }

    private fun updatePassword(password: String) {
        setState { copy(password = password) }
    }

    private fun signUp() {
        setState { copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                signUpUseCase(
                    name = viewState.value.name,
                    email = viewState.value.email,
                    password = viewState.value.password!!
                )
            withContext(Dispatchers.Main) {
                if (result.success)
                    setEffect { Effect.ShowSuccessToast("Create account successfully") }
                else
                    setEffect { Effect.ShowErrorToast(result.errorMessage ?: "") }
                setState { copy(loading = false) }
            }
        }
    }

    private fun navigate() {
        setEffect { Effect.Navigation.ToSignIn }
    }
}