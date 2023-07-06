package com.example.showmagnet.ui.auth.sign_up

import com.example.showmagnet.ui.base.ViewEffect
import com.example.showmagnet.ui.base.ViewEvent
import com.example.showmagnet.ui.base.ViewState

class SignUpContract {
    sealed class Event : ViewEvent {
        object SignUP : Event()

        class NameChanged(val name: String) : Event()

        class EmailChanged(val email: String) : Event()

        class PasswordChanged(val password: String) : Event()
    }

    data class State(
        val loading: Boolean = false,
        val name: String = "",
        val email: String = "",
        val password: String? = null,
    ) : ViewState {
        val isValuedSignUp: Boolean =
            passwordRequirements() == null && name.isNotEmpty() && email.isNotEmpty()
        val passwordRequirement: String? = passwordRequirements()

        private fun passwordRequirements(): String? {
            return password?.let {
                if (password.length < 7) {
                    "Password must be at least 8 characters"
                } else if (password.none { it.isUpperCase() }) {
                    "Password must contain at least one uppercase letter"
                } else if (password.none { it.isDigit() }) {
                    "Password must contain at least one digit"
                } else
                    null
            }
        }
    }

    sealed class Effect : ViewEffect {

        class ShowErrorToast(val message: String) : Effect()
        class ShowSuccessToast(val message: String) : Effect()
    }

    sealed class Navigation {
        object ToSignIn : Navigation()
    }
}