package com.example.showmagnet.ui.common.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show

@Composable
fun ShowsList(
    shows: List<Show>,
    title: String,
    selectedIndex: Int = 0,
    selectionList: List<String> = emptyList(),
    onSelectionChange: (Int) -> Unit = {},
    onItemClick: (Show) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
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
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            items(items = shows, key = { it.id }) { show ->
                ShowItem(
                    url = show.posterPath.baseUrl,
                    title = show.title,
                    rating = show.voteAverage,
                    onItemClick = { onItemClick(show) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ShowsList(
    shows: List<Show>,
    title: String,
    selectedMediaType: MediaType,
    onSelectionChange: (MediaType) -> Unit = {},
    onItemClick: (Show) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp)
                    ),
            ) {
                for (it in MediaType.values()) {
                    val color by animateColorAsState(
                        targetValue = if (it == selectedMediaType) MaterialTheme.colorScheme.secondary
                        else Color.Transparent, label = ""
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (it == selectedMediaType) MaterialTheme.colorScheme.onSecondary
                        else MaterialTheme.colorScheme.onBackground, label = ""
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(color)
                            .clickable { onSelectionChange(it) }
                    ) {
                        Text(
                            text = it.value,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = textColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            items(items = shows, key = { it.id }) { show ->
                ShowItem(
                    url = show.posterPath.baseUrl,
                    title = show.title,
                    rating = show.voteAverage,
                    onItemClick = { onItemClick(show) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
