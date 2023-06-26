package com.example.showmagnet.ui.navigation

import com.google.errorprone.annotations.Immutable

@Immutable
sealed class NavScreen(val route: String) {
    object SignIn : NavScreen("SignIn")
    object SignUp : NavScreen("SignUp")
    object Home : NavScreen("Home")
}