package com.example.showmagnet.ui.navigation.component

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.ui.movie.MovieContract
import com.example.showmagnet.ui.movie.MovieScreen
import com.example.showmagnet.ui.movie.MovieViewModel
import com.example.showmagnet.ui.navigation.AppDestinations

fun NavGraphBuilder.movieScreen(
    onNavigateToMovie: (id: Int) -> Unit,
    onNavigateToTv: (id: Int) -> Unit,
    onNavigateToPerson: (id: Int) -> Unit,
) {
    composable(
        AppDestinations.Movie.route,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) {
        val viewModel = hiltViewModel<MovieViewModel>()
        MovieScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent,
        ) {
            when (it) {
                is MovieContract.Navigation.ToDigitalis -> {
                    when (it.show.type) {
                        MediaType.MOVIE -> onNavigateToMovie(it.show.id)
                        MediaType.TV -> onNavigateToTv(it.show.id)
                    }
                }

                is MovieContract.Navigation.ToPerson -> onNavigateToPerson(it.id)
            }
        }
    }
}