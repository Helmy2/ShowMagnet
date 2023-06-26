package com.example.showmagnet.ui.auth.sign_in

import android.content.IntentSender

data class SignInState(
    val navigateTo: NavigateTo = NavigateTo.None,
    val intentSender: IntentSender? = null,
    val startSignInWithGoogle: Boolean = false,
    val email: String? = null,
    val password: String? = null,
    val loading: Boolean = false,
    val successSignIn: Boolean = false,
    val error: String? = null,
    val success: String? = null,
) {
    fun isFormValid(): Boolean {
        return password?.isNotEmpty() == true &&
                email?.isNotEmpty() == true
    }

    enum class NavigateTo {
        None, SIGN_UP, Home
    }
}


