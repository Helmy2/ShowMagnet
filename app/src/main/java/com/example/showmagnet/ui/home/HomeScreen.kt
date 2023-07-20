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
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.MediaType.MOVIE
import com.example.showmagnet.domain.model.common.MediaType.TV
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.ui.common.ui.ConnectionAndLoadingFeild
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

    LaunchedEffect(key1 = true) {
//        handleEvent(HomeContract.Event.Refresh)
    }

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
        ConnectionAndLoadingFeild(
            connected = state.connected,
            loading = state.loading,
            onRefresh = { handleEvent(HomeContract.Event.Refresh) },
        ) {
            Column(
                Modifier
                    .padding(top = 8.dp)
                    .verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ShowsList(shows = state.upcoming,
                    title = Category.UPCOMING.value,
                    selectedMediaType = state.upcomingMediaType,
                    onSelectionChange = { handleEvent(HomeContract.Event.UpcomingMediaTypeChange(it)) },
                    onItemClick = { navigate(it, handleNavigation) })
                ShowsList(
                    shows = state.trending,
                    title = Category.TRENDING.value,
                    selectedMediaType = state.trendingMediaType,
                    onSelectionChange = { handleEvent(HomeContract.Event.TrendingMediaTypeChange(it)) },
                    onItemClick = { navigate(it, handleNavigation) },
                )
                PersonList(
                    people = state.trendingPeople,
                    title = "Popular People",
                    selectedTimeWindow = state.personTimeWindow,
                    onSelectionChange = { handleEvent(HomeContract.Event.PersonTimeWindowChange(it)) },
                    onItemClick = { handleNavigation(HomeContract.Navigation.ToPerson(it)) },
                )
                ShowsList(
                    shows = state.popular,
                    title = Category.POPULAR.value,
                    selectedMediaType = state.popularMediaType,
                    onSelectionChange = { handleEvent(HomeContract.Event.PopularMediaTypeChange(it)) },
                    onItemClick = { navigate(it, handleNavigation) },
                )
                ShowsList(
                    shows = state.anime,
                    title = Category.ANIME.value,
                    selectedMediaType = state.animeMediaType,
                    onSelectionChange = { handleEvent(HomeContract.Event.AnimeMediaTypeChange(it)) },
                    onItemClick = { navigate(it, handleNavigation) },
                )
            }
        }
    }
}

private fun navigate(show: Show, handleNavigation: (HomeContract.Navigation) -> Unit) {
    when (show.mediaType) {
        MOVIE -> handleNavigation(HomeContract.Navigation.ToMovie(show.id))
        TV -> handleNavigation(HomeContract.Navigation.ToTv(show.id))
    }
}