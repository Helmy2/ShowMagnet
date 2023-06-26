package com.example.showmagnet.ui.auth.sign_up

sealed class SignUpEvent {
    object SignUP : SignUpEvent()

    object NavigateToSignIn : SignUpEvent()
    object NavigateDismissed : SignUpEvent()

    object ErrorDismissed : SignUpEvent()
    class NameChanged(val name: String) :
        SignUpEvent()

    class EmailChanged(val email: String) :
        SignUpEvent()

    class PasswordChanged(val password: String) :
        SignUpEvent()
}