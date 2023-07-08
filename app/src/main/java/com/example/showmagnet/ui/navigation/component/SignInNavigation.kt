package com.example.showmagnet.ui.navigation.component

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.showmagnet.ui.auth.sign_in.SignInContract
import com.example.showmagnet.ui.auth.sign_in.SignInScreen
import com.example.showmagnet.ui.auth.sign_in.SignInViewModel
import com.example.showmagnet.ui.navigation.AppDestinations

fun NavGraphBuilder.signInScreen(
    onNavigateToSignUp: () -> Unit
) {
    composable(AppDestinations.SignIn.route) {
        val viewModel = hiltViewModel<SignInViewModel>()
        SignInScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent
        ) {
            when (it) {
                SignInContract.Navigation.ToSignUp -> onNavigateToSignUp()
            }
        }
    }
}