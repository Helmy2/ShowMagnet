package com.example.showmagnet.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.showmagnet.domain.model.common.MediaType.MOVIE
import com.example.showmagnet.domain.model.common.MediaType.TV
import com.example.showmagnet.domain.model.common.MediaType.values
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.ui.common.ui.ConnectedAndLoadingFeild
import com.example.showmagnet.ui.common.ui.PersonList
import com.example.showmagnet.ui.common.ui.ShowsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    state: HomeContract.State,
    effect: Flow<HomeContract.Effect>,
    handleEvent: (HomeContract.Event) -> Unit,
    handleNavigation: (HomeContract.Navigation) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = effect) {
        effect.collectLatest {
            when (it) {
                is HomeContract.Effect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message, duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) {
        ConnectedAndLoadingFeild(connected = state.connected,
            loading = state.loading,
            onRefresh = { handleEvent(HomeContract.Event.Refresh) }) {
            Column(
                Modifier
                    .padding(top = 8.dp)
                    .verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ShowsList(shows = state.upcoming, title = "Upcoming", onItemClick = {
                    when (it.type) {
                        MOVIE -> handleNavigation(HomeContract.Navigation.ToMovie(it.id))
                        TV -> handleNavigation(HomeContract.Navigation.ToTv(it.id))
                    }
                })
                ShowsList(
                    shows = state.trendingShow,
                    title = "Trending Show",
                    selectedIndex = TimeWindow.values().indexOf(state.trendingShowTimeWindow),
                    selectionList = TimeWindow.values().map { it.formattedValue },
                    onSelectionChange = {
                        handleEvent(
                            HomeContract.Event.TrendingShowTimeWindowChange(
                                TimeWindow.values()[it]
                            )
                        )
                    },
                    onItemClick = {
                        when (it.type) {
                            MOVIE -> handleNavigation(HomeContract.Navigation.ToMovie(it.id))
                            TV -> handleNavigation(HomeContract.Navigation.ToTv(it.id))
                        }
                    },
                )
                PersonList(
                    people = state.tradingPeople,
                    title = "Trading People",
                    selectedIndex = TimeWindow.values().indexOf(state.trendingPersonTimeWindow),
                    selectionList = TimeWindow.values().map { it.formattedValue },
                    onSelectionChange = {
                        handleEvent(
                            HomeContract.Event.TrendingPersonTimeWindowChange(
                                TimeWindow.values()[it]
                            )
                        )
                    },
                    onItemClick = { handleNavigation(HomeContract.Navigation.ToPerson(it)) },
                )
                ShowsList(
                    shows = state.popular,
                    title = "Popular",
                    selectedIndex = values().indexOf(state.popularMediaType),
                    selectionList = values().map { it.name },
                    onSelectionChange = {
                        handleEvent(
                            HomeContract.Event.PopularMediaTypeChange(
                                values()[it]
                            )
                        )
                    },
                    onItemClick = {
                        when (it.type) {
                            MOVIE -> handleNavigation(HomeContract.Navigation.ToMovie(it.id))
                            TV -> handleNavigation(HomeContract.Navigation.ToTv(it.id))
                        }
                    },
                )
                ShowsList(
                    shows = state.anime,
                    title = "Anime",
                    selectedIndex = values().indexOf(state.animeMediaType),
                    selectionList = values().map { it.name },
                    onSelectionChange = {
                        handleEvent(
                            HomeContract.Event.AnimeMediaTypeChange(
                                values()[it]
                            )
                        )
                    },
                    onItemClick = {
                        when (it.type) {
                            MOVIE -> handleNavigation(HomeContract.Navigation.ToMovie(it.id))
                            TV -> handleNavigation(HomeContract.Navigation.ToTv(it.id))
                        }
                    },
                )
            }
        }
    }
}


