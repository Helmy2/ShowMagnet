package com.example.showmagnet.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.showmagnet.ui.movie.MovieScreen
import com.example.showmagnet.ui.movie.MovieViewModel

fun NavGraphBuilder.movieScreen() {
    composable(
        AppDestinations.Movie.route,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) {
        val viewModel = hiltViewModel<MovieViewModel>()
        MovieScreen()
    }
}