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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        Modifier.height(screenHeight * .45f)
    ) {
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
            items(count = if (shows.isEmpty()) 10 else shows.size) { i ->
                ShowItem(
                    url = shows[i].posterPath.baseUrl,
                    title = shows[i].title,
                    rating = shows[i].voteAverage,
                    onItemClick = { onItemClick(shows[i]) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ShowItem(
    url: String,
    title: String,
    rating: Float,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier
        .aspectRatio(.52f)
        .clip(RoundedCornerShape(16.dp))
        .clickable { onItemClick() }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(url).build(),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(.7f),
            )
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            RatingbarFeild(
                rating, Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun ChoiceField(
    selectedIndex: Int,
    selectionList: List<String>,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .border(
                    2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp)
                ),
        ) {
            for (it in selectionList.indices) {
                val color by animateColorAsState(
                    targetValue = if (it == selectedIndex) MaterialTheme.colorScheme.secondary
                    else Color.Transparent, label = ""
                )

                val textColor by animateColorAsState(
                    targetValue = if (it == selectedIndex) MaterialTheme.colorScheme.onSecondary
                    else MaterialTheme.colorScheme.onBackground, label = ""
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(color)
                        .clickable { onItemClicked(it) },
                ) {
                    Text(
                        text = selectionList[it],
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = textColor
                    )
                }
            }
        }
    }
}