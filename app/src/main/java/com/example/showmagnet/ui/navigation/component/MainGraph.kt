package com.example.showmagnet.ui.navigation.component

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.showmagnet.ui.main.MainScreen
import com.example.showmagnet.ui.navigation.AppDestinations

fun NavGraphBuilder.mainScreen(
    onNavigateToMovie: (id: Int) -> Unit,
    onNavigateToTv: (id: Int) -> Unit,
    onNavigateToPeron: (id: Int) -> Unit,
) {
    composable(AppDestinations.Main.route) {
        MainScreen(
            onNavigateToMovie = onNavigateToMovie,
            onNavigateToTv = onNavigateToTv,
            onNavigateToPeron = onNavigateToPeron
        )
    }
}
