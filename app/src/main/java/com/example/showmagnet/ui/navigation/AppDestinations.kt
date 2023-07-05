package com.example.showmagnet.ui.navigation

import com.google.errorprone.annotations.Immutable

@Immutable
sealed class AppDestinations(val route: String) {
    object SignIn : AppDestinations("SignIn")
    object SignUp : AppDestinations("SignUp")
    object Home : AppDestinations("Home")
    object Onboarding : AppDestinations("Onboarding")

    object Movie : AppDestinations("Movie/{id}") {
        fun routeWithID(id: Int) = "Movie/$id"
    }
}