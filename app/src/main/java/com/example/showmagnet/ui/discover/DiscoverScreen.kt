package com.example.showmagnet.ui.discover

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.SortBy
import com.example.showmagnet.ui.common.Constant
import com.example.showmagnet.ui.common.ui.ChipList
import com.example.showmagnet.ui.common.ui.ShowItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiscoveryScreen(
    state: DiscoverContract.State,
    effect: Flow<DiscoverContract.Effect>,
    handleEvent: (DiscoverContract.Event) -> Unit,
    handleNavigation: (DiscoverContract.Navigation) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = effect) {
        effect.collectLatest {
            when (it) {
                is DiscoverContract.Effect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message, duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }



    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            ShowsGrid(
                shows = state.shows,
                shouldLoadMore = { handleEvent(DiscoverContract.Event.LoadMore) },
                onItemClick = {
                    when (it.type) {
                        MediaType.MOVIE -> handleNavigation(DiscoverContract.Navigation.ToMovie(it.id))
                        MediaType.TV -> handleNavigation(DiscoverContract.Navigation.ToTv(it.id))
                    }
                },
            ) {
                DiscoverSetting(
                    mediaType = state.mediaType,
                    selectedGeneraIndex = if (state.mediaType == MediaType.MOVIE) Constant.movieGenreList.indexOf(
                        state.genre
                    ) else Constant.tvGenreList.indexOf(
                        state.genre
                    ),
                    isAdult = state.adult,
                    currentSortBy = state.sortBy,
                    modifier = Modifier.padding(horizontal = 16.dp)

                ) { mediaType, selectedGeneraIndex, adult, sortBy ->
                    handleEvent(
                        DiscoverContract.Event.MediaTypeChange(
                            mediaType, if (selectedGeneraIndex == -1) null
                            else if (mediaType == MediaType.MOVIE) Constant.movieGenreList[selectedGeneraIndex]
                            else Constant.tvGenreList[selectedGeneraIndex], adult, sortBy
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ShowsGrid(
    shows: List<Show>,
    onItemClick: (Show) -> Unit,
    shouldLoadMore: () -> Unit,
    setting: @Composable () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            setting()
        }
        itemsIndexed(items = shows, key = { _, show -> show.id }) { index, show ->
            ShowItem(
                url = show.posterPath.baseUrl,
                title = show.title,
                rating = show.voteAverage,
                onItemClick = { onItemClick(show) },
                modifier = Modifier.padding(8.dp)
            )
            if (index == shows.size - 4)
                shouldLoadMore()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverSetting(
    mediaType: MediaType,
    selectedGeneraIndex: Int,
    isAdult: Boolean,
    currentSortBy: SortBy,
    modifier: Modifier = Modifier,
    onApplyClicked: (selectedMediaType: MediaType, selectedGeneraIndex: Int, adult: Boolean, sortBy: SortBy) -> Unit,
) {

    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var selectedMediaType by remember { mutableStateOf(mediaType) }
    var selectedIndex by remember { mutableStateOf(selectedGeneraIndex) }
    var adult by remember { mutableStateOf(isAdult) }
    var sortBy: SortBy by remember { mutableStateOf(currentSortBy) }

    BackHandler(showDialog) {
        scope.launch { showDialog = false }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(text = "Discover", style = MaterialTheme.typography.titleLarge)
        IconButton(onClick = { showDialog = true }) {
            Icon(
                imageVector = Icons.Default.Settings, contentDescription = "Settings"
            )
        }
    }



    AnimatedVisibility(showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                ChipList(
                    "Choose MediaType",
                    selectedIndex = MediaType.values().indexOf(selectedMediaType),
                    items = MediaType.values().map { it.name },
                    onSelectedIndexChange = { selectedMediaType = MediaType.values()[it] },
                )
                ChipList(
                    "Choose Genre",
                    selectedIndex = selectedIndex,
                    items = if (selectedMediaType == MediaType.MOVIE) Constant.movieGenreList.map { it.name }
                    else Constant.tvGenreList.map { it.name },
                    onSelectedIndexChange = { selectedIndex = it },
                )
                ChipList(
                    "Sort By",
                    selectedIndex = Constant.sortByList.indexOf(sortBy),
                    items = Constant.sortByList.map { it.name },
                    onSelectedIndexChange = { sortBy = Constant.sortByList[it] },
                )
                ChipList(
                    "Adult",
                    selectedIndex = if (adult) 0 else 1,
                    items = listOf("Yes", "No"),
                    onSelectedIndexChange = { adult = it == 0 },
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            showDialog = false
                        }, modifier = Modifier.weight(3f)
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            onApplyClicked(
                                selectedMediaType, selectedIndex, adult, sortBy
                            )
                            showDialog = false
                        }, modifier = Modifier.weight(3f)
                    ) {
                        Text(text = "Apply")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}

