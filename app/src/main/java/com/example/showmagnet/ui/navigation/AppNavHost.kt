package com.example.showmagnet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.showmagnet.ui.navigation.component.mainScreen
import com.example.showmagnet.ui.navigation.component.movieScreen
import com.example.showmagnet.ui.navigation.component.onboardingScreen
import com.example.showmagnet.ui.navigation.component.personScreen
import com.example.showmagnet.ui.navigation.component.signInScreen
import com.example.showmagnet.ui.navigation.component.signUpScreen
import com.example.showmagnet.ui.navigation.component.tvScreen

@Composable
fun AppNavHost(isUserSingedIn: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (isUserSingedIn)
        AppDestinations.Main
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

        mainScreen(
            onNavigateToMovie = { navController.navigateToMovie(it) },
            onNavigateToTv = { navController.navigateToTv(it) },
            onNavigateToPeron = { navController.navigateToPerson(it) }
        )

        movieScreen(
            onNavigateToMovie = { navController.navigateToMovie(it) },
            onNavigateToTv = { navController.navigateToTv(it) },
            onNavigateToPerson = { navController.navigateToPerson(it) }
        )

        personScreen(
            onNavigateToMovie = { navController.navigateToMovie(it) },
            onNavigateToTv = { navController.navigateToTv(it) }
        )

        tvScreen(
            onNavigateToMovie = { navController.navigateToMovie(it) },
            onNavigateToTv = { navController.navigateToTv(it) },
            onNavigateToPerson = { navController.navigateToPerson(it) }
        )
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

fun NavController.navigateToTv(id: Int) {
    this.navigate(AppDestinations.Tv.routeWithID(id))
}

fun NavController.navigateToPerson(id: Int) {
    this.navigate(AppDestinations.Person.routeWithID(id))
}


fun NavController.navigateToMovie(id: Int) {
    this.navigate(AppDestinations.Movie.routeWithID(id))
}