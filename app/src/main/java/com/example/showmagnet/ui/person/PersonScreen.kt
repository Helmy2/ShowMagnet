package com.example.showmagnet.ui.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.ui.common.ExpandableCard
import com.example.showmagnet.ui.common.ImageList
import com.example.showmagnet.ui.common.InfoCard
import com.example.showmagnet.ui.common.ShowsList
import com.example.showmagnet.ui.common.utils.NetworkStatus
import kotlinx.coroutines.flow.Flow

@Composable
fun PersonScreen(
    state: PersonContract.State,
    effect: Flow<PersonContract.Effect>,
    handleEvent: (PersonContract.Event) -> Unit,
    handleNavigation: (PersonContract.Navigation) -> Unit
) {

    Scaffold { padding ->
        if (state.connected == NetworkStatus.Disconnected && state.person == null) Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(text = "No Internet Connection", style = MaterialTheme.typography.titleLarge)
        }
        else {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(padding)
                    .verticalScroll(rememberScrollState(), true)
            ) {
                if (state.person != null) {
                    PersonInf(
                        person = state.person, modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                if (!state.imageList.isNullOrEmpty()) {
                    ImageList(
                        imageList = state.imageList,
                        title = "Images",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                if (!state.movieCredits.isNullOrEmpty()) {
                    ShowsList(
                        title = "Movies",
                        shows = state.movieCredits,
                        loading = false,
                        onItemClick = { handleNavigation(PersonContract.Navigation.ToMovie(it.id)) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                if (!state.tvCredits.isNullOrEmpty()) {
                    ShowsList(
                        title = "Tv Shows",
                        shows = state.tvCredits,
                        loading = false,
                        onItemClick = { handleNavigation(PersonContract.Navigation.ToTv(it.id)) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

    }

}

@Composable
private fun PersonInf(
    person: PersonDetails, modifier: Modifier = Modifier
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
                Text(
                    text = person.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                InfoCard(title = "Known For", body = person.knownForDepartment)
                InfoCard(title = "Popularity", body = person.popularity.toString())
                InfoCard(title = "Birthday", body = person.birthDay)
            }

        }
        Spacer(modifier = Modifier.height(8.dp))

        ExpandableCard("Biography", person.biography)
    }
}


