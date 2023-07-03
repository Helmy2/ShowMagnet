package com.example.showmagnet.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.showmagnet.domain.model.Show

@Composable
fun ShowsList(
    shows: List<Show>,
    title: String,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    selectionList: List<String> = emptyList(),
    onSelectionChange: (Int) -> Unit = {},
    onItemClick: (Show) -> Unit,
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
            items(items = shows, key = { it.id }) {
                MovieItem(
                    url = it.posterPath,
                    title = it.title,
                    rating = it.voteAverage.toFloat(),
                    onItemClick = { onItemClick(it) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}