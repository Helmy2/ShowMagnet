package com.example.showmagnet.ui.movie.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.showmagnet.domain.model.Cast

@Composable
fun CastList(castList: List<Cast>, onCastClick: (id: Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = "Cast",
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(items = castList) { cast ->
                CastItem(
                    url = cast.profilePath.baseUrl,
                    name = cast.name,
                    character = cast.character,
                    id = cast.id,
                    onItemClick = onCastClick,
                    modifier = Modifier
                )
            }
        }
    }
}