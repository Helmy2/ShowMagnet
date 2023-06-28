package com.example.showmagnet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(appNavViewModel: AppNavViewModel) {
    val navController = rememberNavController()
    val isUserSingedIn by appNavViewModel.isUserSingedIn.collectAsState()

    val startDestination = if (isUserSingedIn == true)
        AppDestinations.Home
    else
        AppDestinations.Onboarding

    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        onboardingScreen {
            navController.navigateToSignIn()
        }

        signInScreen(
            onNavigateToSignUp = {
                navController.navigateToSignUp()
            }
        )

        signUpScreen {
            navController.navigateToSignIn()
        }

        homeScreen()

    }
}

fun NavController.navigateToSignIn() {
    this.navigate(AppDestinations.SignIn.route) {
        popUpTo(0)
    }
}

fun NavController.navigateToSignUp() {
    this.navigate(AppDestinations.SignUp.route)
}

