package com.example.showmagnet.ui.tv

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.tv.Episode
import com.example.showmagnet.domain.model.tv.Tv
import com.example.showmagnet.ui.common.ui.CastList
import com.example.showmagnet.ui.common.ui.ConnectedAndLoadingFeild
import com.example.showmagnet.ui.common.ui.ImageList
import com.example.showmagnet.ui.common.ui.InformationFeild
import com.example.showmagnet.ui.common.ui.RatingbarFeild
import com.example.showmagnet.ui.common.ui.ShowsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TvScreen(
    state: TvContract.State,
    effect: Flow<TvContract.Effect>,
    handleEvent: (TvContract.Event) -> Unit,
    handleNavigation: (TvContract.Navigation) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = effect) {
        effect.collectLatest {
            when (it) {
                is TvContract.Effect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message, duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        ConnectedAndLoadingFeild(
            connected = state.connected,
            loading = state.loading,
            onRefresh = { handleEvent(TvContract.Event.Refresh) }
        ) {
            Column(
                modifier = Modifier.verticalScroll(state = rememberScrollState(), enabled = true),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (state.tv != null) {
                    TvDetailsFeild(state.tv, {})
                }
                if (!state.castList.isNullOrEmpty()) {
                    CastList(
                        state.castList,
                    ) { handleNavigation(TvContract.Navigation.ToPerson(it)) }
                }
                if (state.episodeList != null) {
                    SeasonFeild(
                        state.episodeList,
                        numberOfSeasons = state.tv?.numberOfSeasons ?: 1,
                        numberOfSeasonsChange = {
                            handleEvent(
                                TvContract.Event.ChangeNumberOfSeasons(
                                    it
                                )
                            )
                        }
                    )
                }
                if (!state.imageList.isNullOrEmpty()) {
                    ImageList(
                        state.imageList,
                        "Images",
                    )
                }
                if (!state.recommendations.isNullOrEmpty()) {
                    ShowsList(
                        shows = state.recommendations,
                        title = "Recommendations",
                        onItemClick = {
                            when (it.type) {
                                MediaType.MOVIE -> handleNavigation(TvContract.Navigation.ToMovie(it.id))
                                MediaType.TV -> handleNavigation(TvContract.Navigation.ToTv(it.id))
                            }
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonFeild(
    episodeList: List<Episode>,
    numberOfSeasons: Int,
    numberOfSeasonsChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(1) }
    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Episodes", style = MaterialTheme.typography.titleMedium
            )
            Box {
                Card(onClick = { expanded = true }) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Season $selectedIndex",
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                        )
                    }
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    for (i in 1..numberOfSeasons) {
                        DropdownMenuItem(text = { Text(text = "Season $i") }, onClick = {
                            selectedIndex = i
                            numberOfSeasonsChange(i)
                            expanded = false
                        })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            items(episodeList) {
                EpisodeItem(
                    episode = it, modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EpisodeItem(episode: Episode, modifier: Modifier = Modifier) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Card(
        modifier = modifier
            .height(screenHeight * .45f)
            .aspectRatio(.8f)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(episode.stillPath.baseUrl)
                    .crossfade(true).build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight(.6f),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = episode.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(3f)
                )
                RatingbarFeild(
                    episode.voteAverage.toFloat(), Modifier.weight(2f)
                )
            }
            Text(
                text = episode.overview,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

}

@Composable
private fun TvDetailsFeild(
    tv: Tv,
    onGenreClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(tv.backdropPath.baseUrl)
                .crossfade(true).build(),
            contentDescription = tv.name,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.7f),
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        .3f to Color.Transparent,
                        .7f to MaterialTheme.colorScheme.background,
                    )
                )
                .fillMaxSize()
        )
        InformationFeild(
            title = tv.name,
            releaseDate = tv.firstAirDate ?: "",
            voteAverage = tv.voteAverage,
            onGenreClick = onGenreClick,
            genres = tv.genres,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}