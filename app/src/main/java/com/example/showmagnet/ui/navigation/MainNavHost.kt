package com.example.showmagnet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.showmagnet.ui.auth.sign_up.SignUpScreen
import com.example.showmagnet.ui.auth.sign_up.SignUpState
import com.example.showmagnet.ui.auth.sign_up.SignUpViewModel

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavScreen.SignIn.route,
    ) {
        signUpScreen {
            navController.navigateToSignIn()
        }

        signInScreen {
            navController.navigateToSignUp()
        }
    }
}

fun NavController.navigateToSignIn() {
    this.popBackStack(NavScreen.SignIn.route, false)
}

fun NavController.navigateToSignUp() {
    this.navigate(NavScreen.SignUp.route)
}

fun NavGraphBuilder.signInScreen(
    onNavigateToNextScreen: () -> Unit
) {
    composable(NavScreen.SignIn.route) {

    }
}

fun NavGraphBuilder.signUpScreen(
    onNavigateToNextScreen: () -> Unit
) {
    composable(NavScreen.SignUp.route) {
        val viewModel = hiltViewModel<SignUpViewModel>()
        val state: SignUpState by viewModel.uiState.collectAsState()

        SideEffect {
            if (state.navigateToNextScreen)
                onNavigateToNextScreen()
        }

        SignUpScreen(
            state = state,
            handleEvent = viewModel::handleEvent
        )
    }
}