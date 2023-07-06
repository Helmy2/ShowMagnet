package com.example.showmagnet.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.ui.home.components.ChoiceField

@Composable
fun ShowsList(
    shows: List<Show>,
    title: String,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    selectionList: List<String> = emptyList(),
    onSelectionChange: (Int) -> Unit = {},
    onItemClick: (Show) -> Unit,
    loading: Boolean,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        modifier.height(screenHeight * .45f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            ChoiceField(
                selectedIndex = selectedIndex,
                selectionList = selectionList,
                onItemClicked = onSelectionChange
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(count = if (shows.isEmpty()) 10 else shows.size) { i ->
                ShimmerListItem(
                    isLoading = loading || shows.isEmpty(),
                    contentAfterLoading = {
                        ShowItem(
                            loading = false,
                            url = shows[i].posterPath.baseUrl,
                            title = shows[i].title,
                            rating = shows[i].voteAverage.toFloat(),
                            onItemClick = { onItemClick(shows[i]) },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    },
                    contentBeforeLoading = {
                        ShowItem(
                            loading = true,
                            url = "",
                            title = "",
                            rating = 0f,
                            onItemClick = { },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                )
            }
        }
    }
}


@Composable
fun ShimmerListItem(
    isLoading: Boolean,
    contentAfterLoading: @Composable (Modifier) -> Unit,
    contentBeforeLoading: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Crossfade(targetState = isLoading, label = "") {
        if (it) {
            contentBeforeLoading(modifier)
        } else {
            contentAfterLoading(modifier)
        }
    }
}