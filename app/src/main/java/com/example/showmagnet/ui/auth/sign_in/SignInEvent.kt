package com.example.showmagnet.ui.auth.sign_in

import android.content.Intent

sealed class SignInEvent {
    object SignInWithEmail : SignInEvent()
    object StartSignInWithGoogle : SignInEvent()
    class ResetPassword(val email: String) : SignInEvent()
    class SignInWithGoogle(val intent: Intent) : SignInEvent()

    object NavigateDismissed : SignInEvent()

    object NavigateToSignUp : SignInEvent()

    object NavigateToHome : SignInEvent()

    object ErrorDismissed : SignInEvent()

    class EmailChanged(val email: String) :
        SignInEvent()

    class PasswordChanged(val password: String) :
        SignInEvent()
}