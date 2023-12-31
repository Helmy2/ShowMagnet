package com.example.showmagnet.ui.navigation.component

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.showmagnet.ui.auth.sign_up.SignUpContract
import com.example.showmagnet.ui.auth.sign_up.SignUpScreen
import com.example.showmagnet.ui.auth.sign_up.SignUpViewModel
import com.example.showmagnet.ui.navigation.AppDestinations

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
                SignUpContract.Navigation.ToSignIn -> onNavigateToSignIn()
            }
        }
    }
}