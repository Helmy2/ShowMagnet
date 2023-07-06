package com.example.showmagnet.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.showmagnet.ui.person.PersonContract
import com.example.showmagnet.ui.person.PersonScreen
import com.example.showmagnet.ui.person.PersonViewModel


fun NavGraphBuilder.personScreen(
    onNavigateToMovie: (id: Int) -> Unit,
    onNavigateToTv: (id: Int) -> Unit,
) {
    composable(
        AppDestinations.Person.route,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) {
        val viewModel = hiltViewModel<PersonViewModel>()
        PersonScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent
        ) {
            when (it) {
                is PersonContract.Navigation.ToMovie -> onNavigateToMovie(it.id)
                is PersonContract.Navigation.ToTv -> onNavigateToTv(it.id)
            }
        }
    }
}