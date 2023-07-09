package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showmagnet.domain.model.common.Genre
import com.example.showmagnet.ui.common.utils.toYearFormat

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InformationFeild(
    modifier: Modifier = Modifier,
    title: String,
    favorite: Boolean,
    runtime: String? = null,
    voteAverage: Float,
    genres: List<Genre>,
    releaseDate: String,
    onGenreClick: (id: Int) -> Unit,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Ratingbar(rating = voteAverage, modifier = Modifier.width(80.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = voteAverage.toString(), style = MaterialTheme.typography.titleMedium)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
                    .copy(fontSize = 20.sp),
                maxLines = 2,
                modifier = Modifier.weight(fill = false, weight = 1f)
            )
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (favorite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite Button",
                )
            }
        }
        FlowRow {
            runtime?.let {
                Text(text = runtime, modifier = Modifier.padding(4.dp))
            }
            Chip(
                text = releaseDate.toYearFormat(),
                onItemClick = {},
                modifier = Modifier.padding(4.dp)
            )
            genres.forEach {
                Chip(
                    text = it.name,
                    onItemClick = { onGenreClick(it.id) },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}