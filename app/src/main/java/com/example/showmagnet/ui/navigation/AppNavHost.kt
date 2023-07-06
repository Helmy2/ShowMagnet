package com.example.showmagnet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(isUserSingedIn: Boolean) {
    val navController = rememberNavController()

    val startDestination = if (isUserSingedIn)
        AppDestinations.Home
    else
        AppDestinations.Onboarding

    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        onboardingScreen(
            onNavigateToSignIn = { navController.navigateToSignIn() }
        )

        signInScreen(
            onNavigateToSignUp = { navController.navigateToSignUp() }
        )

        signUpScreen(
            onNavigateToSignIn = { navController.navigateToSignIn() }
        )

        homeScreen(
            onNavigateToMovie = { navController.navigateToMovie(it) },
            onNavigateToTv = {}
        )

        movieScreen(
            onNavigateToMovie = { navController.navigateToMovie(it) },
            onNavigateToTv = {},
            onNavigateToPerson = { navController.navigateToPerson(it) }
        )

        personScreen(
            onNavigateToMovie = { navController.navigateToMovie(it) },
            onNavigateToTv = {}
        )

    }
}

fun NavController.navigateToPerson(id: Int) {
    this.navigate(AppDestinations.Person.routeWithID(id))
}


fun NavController.navigateToMovie(id: Int) {
    this.navigate(AppDestinations.Movie.routeWithID(id))
}

fun NavController.navigateToSignIn() {
    this.navigate(AppDestinations.SignIn.route) {
        popUpTo(0)
    }
}

fun NavController.navigateToSignUp() {
    this.navigate(AppDestinations.SignUp.route)
}

