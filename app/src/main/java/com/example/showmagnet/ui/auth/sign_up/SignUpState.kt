package com.example.showmagnet.ui.auth.sign_up

import androidx.annotation.StringRes
import com.example.showmagnet.R

data class SignUpState(
    val navigateToNextScreen: Boolean = false,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val passwordRequirements: List<PasswordRequirement> = emptyList(),
    val loading: Boolean = false,
    val successSignUp: Boolean = false,
    val error: String? = null,
) {
    fun isFormValid(): Boolean {
        return name?.isNotEmpty() == true &&
                password?.isNotEmpty() == true &&
                email?.isNotEmpty() == true &&
                !passwordRequirements.containsAll(
                    PasswordRequirement.values().toList()
                )
    }
}

enum class PasswordRequirement(
    @StringRes val label: Int
) {
    EIGHT_CHARACTERS(R.string.password_requirement_characters),
    CAPITAL_LETTER(R.string.password_requirement_capital),
    NUMBER(R.string.password_requirement_digit),
}