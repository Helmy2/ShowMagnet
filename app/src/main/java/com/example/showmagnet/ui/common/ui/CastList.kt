package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.showmagnet.domain.model.common.Cast

@Composable
fun CastList(
    castList: List<Cast>, onCastClick: (id: Int) -> Unit
) {
    Column {
        Text(
            text = "Cast",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            items(items = castList) { cast ->
                CastItem(
                    url = cast.profilePath.baseUrl,
                    name = cast.name,
                    character = cast.character,
                    id = cast.id,
                    onItemClick = onCastClick,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CastItem(
    url: String,
    name: String,
    character: String?,
    id: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .clickable { onItemClick(id) }
        .width(120.dp)
        .aspectRatio(.52f)) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true)
                    .build(),
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp),
            )
            character?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(
                        start = 8.dp, end = 8.dp, bottom = 8.dp
                    )
                )
            }
        }
    }
}