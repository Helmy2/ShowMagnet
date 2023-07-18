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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person

@Composable
fun PersonList(
    people: List<Person>, title: String,
    selectedIndex: Int = 0,
    selectionList: List<String> = emptyList(),
    onSelectionChange: (Int) -> Unit = {},
    onItemClick: (id: Int) -> Unit,
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
            items(items = people, key = { it.id }) { person ->
                PersonItem(
                    url = person.profilePath.baseUrl,
                    name = person.name,
                    onItemClick = { onItemClick(person.id) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun PersonList(
    people: List<Person>, title: String,
    selectedTimeWindow: TimeWindow,
    onSelectionChange: (TimeWindow) -> Unit,
    onItemClick: (id: Int) -> Unit,
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
                for (it in TimeWindow.values()) {
                    val color by animateColorAsState(
                        targetValue = if (it == selectedTimeWindow) MaterialTheme.colorScheme.secondary
                        else Color.Transparent, label = ""
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (it == selectedTimeWindow) MaterialTheme.colorScheme.onSecondary
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
            items(items = people, key = { it.id }) { person ->
                PersonItem(
                    url = person.profilePath.baseUrl,
                    name = person.name,
                    onItemClick = { onItemClick(person.id) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun PersonItem(
    url: String, name: String, onItemClick: () -> Unit, modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .clickable { onItemClick() }
        .width(120.dp)
        .aspectRatio(.52f)) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true)
                    .build(),
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}