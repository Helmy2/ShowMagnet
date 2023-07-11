package com.example.showmagnet.ui.navigation

import com.google.errorprone.annotations.Immutable

@Immutable
sealed class AppDestinations(val route: String) {
    object Onboarding : AppDestinations("Onboarding")
    object SignIn : AppDestinations("SignIn")
    object SignUp : AppDestinations("SignUp")

    object Main : AppDestinations("Main")
    object Home : AppDestinations("Home")
    object Favorite : AppDestinations("Favorite")
    object Discover : AppDestinations("Discover")
    object Profile : AppDestinations("Profile")

    object Movie : AppDestinations("Movie/{id}") {
        fun routeWithID(id: Int) = "Movie/$id"
    }

    object Person : AppDestinations("Person/{id}") {
        fun routeWithID(id: Int) = "Person/$id"
    }

    object Tv : AppDestinations("Tv/{id}") {
        fun routeWithID(id: Int) = "Tv/$id"
    }
}