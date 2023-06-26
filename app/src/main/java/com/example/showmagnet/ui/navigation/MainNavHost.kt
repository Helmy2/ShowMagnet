package com.example.showmagnet.ui.navigation

import androidx.compose.material3.Text
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
import com.example.showmagnet.ui.auth.sign_in.SignInEvent
import com.example.showmagnet.ui.auth.sign_in.SignInScreen
import com.example.showmagnet.ui.auth.sign_in.SignInState
import com.example.showmagnet.ui.auth.sign_in.SignInViewModel
import com.example.showmagnet.ui.auth.sign_up.SignUpEvent
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

        signInScreen(
            onNavigateToHome = {
                navController.navigateToHomeUp()
            },
            onNavigateToSignUp = {
                navController.navigateToSignUp()
            }
        )

        composable(NavScreen.Home.route) {
            Text(text = "Home")
        }
    }
}

fun NavController.navigateToHomeUp() {
    this.navigate(NavScreen.Home.route)
}

fun NavController.navigateToSignIn() {
    this.popBackStack(NavScreen.SignIn.route, false)
}

fun NavController.navigateToSignUp() {
    this.navigate(NavScreen.SignUp.route)
}

fun NavGraphBuilder.signInScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    composable(NavScreen.SignIn.route) {
        val viewModel = hiltViewModel<SignInViewModel>()
        val state by viewModel.uiState.collectAsState()

        SideEffect {
            when (state.navigateTo) {
                SignInState.NavigateTo.SIGN_UP -> {
                    onNavigateToSignUp()
                    viewModel.handleEvent(SignInEvent.NavigateDismissed)
                }

                SignInState.NavigateTo.Home -> {
                    onNavigateToHome()
                    viewModel.handleEvent(SignInEvent.NavigateDismissed)
                }

                else -> {}
            }
        }

        SignInScreen(
            state = state,
            handleEvent = viewModel::handleEvent
        )
    }
}

fun NavGraphBuilder.signUpScreen(
    onNavigateToSignIn: () -> Unit
) {
    composable(NavScreen.SignUp.route) {
        val viewModel = hiltViewModel<SignUpViewModel>()
        val state: SignUpState by viewModel.uiState.collectAsState()

        SideEffect {
            if (state.navigateToNextScreen) {
                onNavigateToSignIn()
                viewModel.handleEvent(SignUpEvent.NavigateDismissed)
            }
        }

        SignUpScreen(
            state = state,
            handleEvent = viewModel::handleEvent
        )
    }
}