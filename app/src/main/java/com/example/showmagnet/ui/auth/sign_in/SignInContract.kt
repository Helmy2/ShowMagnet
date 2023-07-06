package com.example.showmagnet.ui.auth.sign_in

import android.content.Intent
import android.content.IntentSender
import com.example.showmagnet.ui.common.base.ViewEffect
import com.example.showmagnet.ui.common.base.ViewEvent
import com.example.showmagnet.ui.common.base.ViewState

class SignInContract {
    sealed class Event : ViewEvent {
        object SignInWithEmail : Event()
        object StartSignInWithGoogle : Event()
        class ResetPassword(val email: String) : Event()
        class SignInWithGoogle(val intent: Intent) : Event()
        class EmailChanged(val email: String) : Event()
        class PasswordChanged(val password: String) : Event()
    }


    data class State(
        val loadingWithEmail: Boolean = false,
        val loadingWithGoogle: Boolean = false,
        val email: String = "",
        val password: String = "",
    ) : ViewState {
        val isValuedSignUp: Boolean = password.isNotEmpty() && email.isNotEmpty()
    }

    sealed class Effect : ViewEffect {
        class ShowSuccessToast(val message: String) : Effect()
        class ShowErrorToast(val message: String) : Effect()
        class StartSignInWithGoogle(val intentSender: IntentSender?) : Effect()

    }

    sealed class Navigation {
        object ToSignUp : Navigation()
    }
}