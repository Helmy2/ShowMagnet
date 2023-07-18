package com.example.showmagnet.ui.favorite

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
import com.example.showmagnet.ui.common.ui.ConnectionAndLoadingFeild
import com.example.showmagnet.ui.common.ui.PersonList
import com.example.showmagnet.ui.common.ui.ShowsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteScreen(
    state: FavoriteContract.State,
    effect: Flow<FavoriteContract.Effect>,
    handleEvent: (FavoriteContract.Event) -> Unit,
    handleNavigation: (FavoriteContract.Navigation) -> Unit
) {

    LaunchedEffect(key1 = true){
        handleEvent(FavoriteContract.Event.Refresh)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = effect) {
        effect.collectLatest {
            when (it) {
                is FavoriteContract.Effect.ShowErrorToast -> {
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
        ConnectionAndLoadingFeild(connected = state.connected,
            loading = state.loading,
            onRefresh = { handleEvent(FavoriteContract.Event.Refresh) }) {
            Column(
                Modifier
                    .padding(top = 8.dp)
                    .verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (state.movie.isNotEmpty())
                    ShowsList(
                        shows = state.movie,
                        title = "Favorite Movies",
                        onItemClick = {
                            when (it.mediaType) {
                                MOVIE -> handleNavigation(FavoriteContract.Navigation.ToMovie(it.id))
                                TV -> handleNavigation(FavoriteContract.Navigation.ToTv(it.id))
                            }
                        })
                if (state.tv.isNotEmpty())
                    ShowsList(
                        shows = state.tv,
                        title = "Favorite TV Shows",
                        onItemClick = {
                            when (it.mediaType) {
                                MOVIE -> handleNavigation(FavoriteContract.Navigation.ToMovie(it.id))
                                TV -> handleNavigation(FavoriteContract.Navigation.ToTv(it.id))
                            }
                        })
                if (state.people.isNotEmpty())
                    PersonList(
                        people = state.people,
                        title = "Favorite People",
                        onItemClick = { handleNavigation(FavoriteContract.Navigation.ToPerson(it)) },
                    )
            }
        }
    }
}


