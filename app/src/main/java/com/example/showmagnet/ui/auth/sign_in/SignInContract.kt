package com.example.showmagnet.ui.auth.sign_in

import android.content.Intent
import android.content.IntentSender
import com.example.showmagnet.ui.base.ViewEffect
import com.example.showmagnet.ui.base.ViewEvent
import com.example.showmagnet.ui.base.ViewState

class SignInContract {
    sealed class Event : ViewEvent {

        object SignInWithEmail : Event()
        object StartSignInWithGoogle : Event()

        object NavigateToSignUp : Event()
        class ResetPassword(val email: String) : Event()
        class SignInWithGoogle(val intent: Intent) : Event()
        class EmailChanged(val email: String) : Event()

        class PasswordChanged(val password: String) : Event()
    }


    data class State(
        val loading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val intentSender: IntentSender? = null,
    ) : ViewState {
        val isValuedSignUp: Boolean = password.isNotEmpty() && email.isNotEmpty()
    }

    sealed class Effect : ViewEffect {
        class ShowSuccessToast(val message: String) : Effect()
        class ShowErrorToast(val message: String) : Effect()
        object StartSignInWithGoogle : Effect()

        sealed class Navigation : Effect() {
            object ToHome : Navigation()
            object ToSignUp : Navigation()
        }
    }
}