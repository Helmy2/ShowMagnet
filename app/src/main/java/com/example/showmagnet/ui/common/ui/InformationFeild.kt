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
    runtime: String? = null,
    voteAverage: Float,
    genres: List<Genre>,
    releaseDate: String,
    onGenreClick: (id: Int) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Ratingbar(rating = voteAverage, modifier = Modifier.width(80.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = voteAverage.toString(), style = MaterialTheme.typography.titleMedium)
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 25.sp),
            maxLines = 2
        )
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