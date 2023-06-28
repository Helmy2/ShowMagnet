package com.example.showmagnet.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.showmagnet.ui.auth.sign_up.SignUpContract
import com.example.showmagnet.ui.auth.sign_up.SignUpScreen
import com.example.showmagnet.ui.auth.sign_up.SignUpViewModel

fun NavGraphBuilder.signUpScreen(
    onNavigateToSignIn: () -> Unit
) {
    composable(AppDestinations.SignUp.route) {
        val viewModel = hiltViewModel<SignUpViewModel>()

        SignUpScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent
        ) {
            when (it) {
                SignUpContract.Effect.Navigation.ToSignIn -> onNavigateToSignIn()
            }
        }
    }
}