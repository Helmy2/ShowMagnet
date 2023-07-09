package com.example.showmagnet.ui.navigation.component

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.showmagnet.ui.home.HomeContract
import com.example.showmagnet.ui.home.HomeScreen
import com.example.showmagnet.ui.home.HomeViewModel
import com.example.showmagnet.ui.navigation.AppDestinations

fun NavGraphBuilder.homeScreen(
    onNavigateToMovie: (id: Int) -> Unit,
    onNavigateToTv: (id: Int) -> Unit,
    onNavigateToPeron: (id: Int) -> Unit,
) {
    composable(AppDestinations.Home.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent,
        ) {
            when (it) {
                is HomeContract.Navigation.ToMovie -> onNavigateToMovie(it.id)
                is HomeContract.Navigation.ToPerson -> onNavigateToPeron(it.id)
                is HomeContract.Navigation.ToTv -> onNavigateToTv(it.id)
            }
        }
    }
}