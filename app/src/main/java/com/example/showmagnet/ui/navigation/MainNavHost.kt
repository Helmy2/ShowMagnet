package com.example.showmagnet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.showmagnet.ui.discover.DiscoverContract
import com.example.showmagnet.ui.discover.DiscoverViewModel
import com.example.showmagnet.ui.discover.DiscoveryScreen
import com.example.showmagnet.ui.favorite.FavoriteContract
import com.example.showmagnet.ui.favorite.FavoriteScreen
import com.example.showmagnet.ui.favorite.FavoriteViewModel
import com.example.showmagnet.ui.navigation.component.homeScreen
import com.example.showmagnet.ui.profile.ProfileScreen
import com.example.showmagnet.ui.profile.ProfileViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    onNavigateToMovie: (id: Int) -> Unit,
    onNavigateToTv: (id: Int) -> Unit,
    onNavigateToPeron: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.Home.route,
        modifier = modifier
    ) {
        homeScreen(
            onNavigateToMovie = onNavigateToMovie,
            onNavigateToTv = onNavigateToTv,
            onNavigateToPeron = onNavigateToPeron
        )

        favoriteScreen(
            onNavigateToMovie = onNavigateToMovie,
            onNavigateToTv = onNavigateToTv,
            onNavigateToPeron = onNavigateToPeron
        )

        profileScreen()

        discoverScreen(
            onNavigateToMovie = onNavigateToMovie, onNavigateToTv = onNavigateToTv
        )
    }
}

fun NavGraphBuilder.discoverScreen(
    onNavigateToMovie: (id: Int) -> Unit,
    onNavigateToTv: (id: Int) -> Unit,
) {
    composable(AppDestinations.Discover.route) {
        val viewModel = hiltViewModel<DiscoverViewModel>()

        DiscoveryScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent
        ) {
            when (it) {
                is DiscoverContract.Navigation.ToMovie -> onNavigateToMovie(it.id)
                is DiscoverContract.Navigation.ToTv -> onNavigateToTv(it.id)
            }
        }
    }
}


fun NavGraphBuilder.profileScreen() {
    composable(AppDestinations.Profile.route) {
        val viewModel = hiltViewModel<ProfileViewModel>()

        ProfileScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent
        )
    }
}


fun NavGraphBuilder.favoriteScreen(
    onNavigateToMovie: (id: Int) -> Unit,
    onNavigateToTv: (id: Int) -> Unit,
    onNavigateToPeron: (id: Int) -> Unit
) {
    composable(AppDestinations.Favorite.route) {
        val viewModel = hiltViewModel<FavoriteViewModel>()
        FavoriteScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent,
        ) {
            when (it) {
                is FavoriteContract.Navigation.ToMovie -> onNavigateToMovie(it.id)
                is FavoriteContract.Navigation.ToPerson -> onNavigateToPeron(it.id)
                is FavoriteContract.Navigation.ToTv -> onNavigateToTv(it.id)
            }
        }
    }
}


