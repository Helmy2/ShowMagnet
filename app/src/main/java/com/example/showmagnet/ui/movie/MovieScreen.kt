package com.example.showmagnet.ui.movie

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.showmagnet.domain.model.Movie
import com.example.showmagnet.ui.common.ImageList
import com.example.showmagnet.ui.common.InformationFeild
import com.example.showmagnet.ui.common.ShowsList
import com.example.showmagnet.ui.common.utils.NetworkStatus
import com.example.showmagnet.ui.common.utils.toHourFormat
import com.example.showmagnet.ui.movie.components.CastList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieScreen(
    state: MovieContract.State,
    effect: Flow<MovieContract.Effect>,
    handleEvent: (MovieContract.Event) -> Unit,
    handleNavigation: (MovieContract.Navigation) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = effect) {
        effect.collectLatest {
            when (it) {
                is MovieContract.Effect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        if (state.connected == NetworkStatus.Disconnected && state.movie == null) Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(text = "No Internet Connection", style = MaterialTheme.typography.titleLarge)
        }
        else Column(
            modifier = Modifier.verticalScroll(state = rememberScrollState(), enabled = true),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (state.movie != null) {
                MovieDetailsFeild(state.movie, {})
                StorylineFeild(
                    state.movie.overview, modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            if (!state.castList.isNullOrEmpty()) {
                CastList(
                    state.castList, { handleNavigation(MovieContract.Navigation.ToPerson(it)) },
                    Modifier.padding(horizontal = 16.dp)
                )
            }
            if (!state.collection.isNullOrEmpty()) {
                ShowsList(
                    shows = state.collection,
                    title = "Collection",
                    loading = false,
                    onItemClick = { handleNavigation(MovieContract.Navigation.ToDigitalis(it)) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            if (!state.imageList.isNullOrEmpty()) {
                ImageList(
                    state.imageList,
                    "Images",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            if (!state.recommendations.isNullOrEmpty()) {
                ShowsList(
                    shows = state.recommendations,
                    title = "Recommendations",
                    loading = false,
                    onItemClick = { handleNavigation(MovieContract.Navigation.ToDigitalis(it)) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun StorylineFeild(overview: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = "Storyline",
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = overview, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun MovieDetailsFeild(
    movie: Movie,
    onGenreClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(movie.backdropPath.baseUrl)
                .crossfade(true).build(),
            contentDescription = movie.title,
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
            title = movie.title,
            runtime = movie.runtime.toHourFormat(),
            releaseDate = movie.releaseDate,
            voteAverage = movie.voteAverage.toFloat(),
            onGenreClick = onGenreClick,
            genres = movie.genres,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}


