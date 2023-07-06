package com.example.showmagnet.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.showmagnet.ui.tv.TvScreen
import com.example.showmagnet.ui.tv.TvViewModel

fun NavGraphBuilder.tvScreen(
    onNavigateToMovie: (id: Int) -> Unit,
    onNavigateToTv: (id: Int) -> Unit,
    onNavigateToPerson: (id: Int) -> Unit,
) {
    composable(
        AppDestinations.Tv.route,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) {
        val viewModel = hiltViewModel<TvViewModel>()
        TvScreen()
    }
}