package com.example.showmagnet.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.showmagnet.ui.home.components.Ratingbar
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun ShowItem(
    url: String,
    title: String,
    rating: Float,
    loading: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(url)
            .build()
    )

    Card(modifier = modifier
        .aspectRatio(.52f)
        .clip(RoundedCornerShape(16.dp))
        .clickable { onItemClick() }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(.7f)
                    .placeholder(
                        visible = loading,
                        highlight = PlaceholderHighlight.shimmer()
                    ),
            )
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .placeholder(
                        visible = loading,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
            RatingbarFeild(
                rating,
                Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .placeholder(
                        visible = loading,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
        }
    }

}

@Composable
fun RatingbarFeild(rating: Float, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()
    ) {
        Ratingbar(
            rating, modifier = Modifier
                .weight(7f)
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        RatingAutoSizeText(
            text = "%.1f".format(rating), modifier = Modifier.weight(2f)
        )
    }
}


@Composable
fun RatingAutoSizeText(
    text: String, modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f), contentAlignment = Alignment.Center
    ) {
        val size = maxWidth.value
        val fontSize: Float = size / text.length * 5.4f
        Text(
            text = text,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            fontSize = LocalDensity.current.run { fontSize.toSp() },
            textAlign = TextAlign.Center,
        )
    }
}
