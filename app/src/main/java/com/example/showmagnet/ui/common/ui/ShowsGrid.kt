package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.showmagnet.domain.model.common.Show

@Composable
fun ShowsGrid(
    shows: List<Show>,
    onItemClick: (Show) -> Unit,
    shouldLoadMore: () -> Unit,
    topBar: @Composable () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            topBar()
        }
        itemsIndexed(items = shows, key = { _, show -> show.id }) { index, show ->
            ShowItem(
                url = show.posterPath.baseUrl,
                title = show.title,
                rating = show.voteAverage,
                onItemClick = { onItemClick(show) },
                modifier = Modifier.padding(8.dp)
            )
            if (shows.size >= 20 && index == shows.size - 4)
                shouldLoadMore()
        }
    }
}