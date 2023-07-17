package com.example.showmagnet.ui.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.ui.common.ui.ConnectionAndLoadingFeild
import com.example.showmagnet.ui.common.ui.ExpandableCard
import com.example.showmagnet.ui.common.ui.FlowCard
import com.example.showmagnet.ui.common.ui.ImageList
import com.example.showmagnet.ui.common.ui.ShowsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PersonScreen(
    state: PersonContract.State,
    effect: Flow<PersonContract.Effect>,
    handleEvent: (PersonContract.Event) -> Unit,
    handleNavigation: (PersonContract.Navigation) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = effect) {
        effect.collectLatest {
            when (it) {
                is PersonContract.Effect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message, duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        ConnectionAndLoadingFeild(
            connected = state.connected,
            loading = state.loading,
            onRefresh = { handleEvent(PersonContract.Event.Refresh) }
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(padding)
                    .verticalScroll(rememberScrollState(), true)
            ) {
                if (state.person != null) {
                    PersonInf(
                        person = state.person,
                        onFavoriteClick = { handleEvent(PersonContract.Event.ToggleFavorite) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                if (!state.imageList.isNullOrEmpty()) {
                    ImageList(
                        imageList = state.imageList,
                        title = "Images",
                    )
                }
                if (!state.movieCredits.isNullOrEmpty()) {
                    ShowsList(
                        title = "Movies",
                        shows = state.movieCredits,
                        onItemClick = { handleNavigation(PersonContract.Navigation.ToMovie(it.id)) },
                    )
                }
                if (!state.tvCredits.isNullOrEmpty()) {
                    ShowsList(
                        title = "Tv Shows",
                        shows = state.tvCredits,
                        onItemClick = { handleNavigation(PersonContract.Navigation.ToTv(it.id)) },
                    )
                }
            }
        }
    }

}


@Composable
private fun PersonInf(
    person: PersonDetails,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(person.profilePath.baseUrl)
                    .crossfade(true).build(),
                contentDescription = "",
                modifier = Modifier
                    .weight(2f)
                    .aspectRatio(person.profilePath.ratio),
                alignment = Alignment.TopCenter,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(fill = false, weight = 1f)

                    )
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (person.favorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite Button",
                        )
                    }
                }

                FlowCard(title = "Known For", body = person.knownForDepartment)
                FlowCard(title = "Popularity", body = person.popularity.toString())
                FlowCard(title = "Birthday", body = person.birthDay)
            }

        }
        Spacer(modifier = Modifier.height(8.dp))

        ExpandableCard("Biography", person.biography)
    }
}


