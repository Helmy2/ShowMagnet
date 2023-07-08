package com.example.showmagnet.ui.navigation.component

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.showmagnet.ui.navigation.AppDestinations
import com.example.showmagnet.ui.tv.TvContract
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
        TvScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent
        ) {
            when (it) {
                is TvContract.Navigation.ToMovie -> onNavigateToMovie(it.id)
                is TvContract.Navigation.ToPerson -> onNavigateToPerson(it.id)
                is TvContract.Navigation.ToTv -> onNavigateToTv(it.id)
            }
        }
    }
}