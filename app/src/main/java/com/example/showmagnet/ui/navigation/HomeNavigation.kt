package com.example.showmagnet.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.showmagnet.ui.home.HomeScreen
import com.example.showmagnet.ui.home.HomeViewModel

fun NavGraphBuilder.homeScreen() {
    composable(AppDestinations.Home.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen()
    }
}