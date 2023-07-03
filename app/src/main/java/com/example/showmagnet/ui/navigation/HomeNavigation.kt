package com.example.showmagnet.ui.navigation

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.showmagnet.ui.home.HomeContract
import com.example.showmagnet.ui.home.HomeScreen
import com.example.showmagnet.ui.home.HomeViewModel

fun NavGraphBuilder.homeScreen() {
    composable(AppDestinations.Home.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val context = LocalContext.current
        HomeScreen(
            state = viewModel.viewState.value,
            effect = viewModel.effect,
            handleEvent = viewModel::setEvent,
        ) {
            when (it) {
                is HomeContract.Effect.Navigation.ToDigitalis -> {
                    Toast.makeText(context, it.show.title, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}