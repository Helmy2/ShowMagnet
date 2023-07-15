package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.showmagnet.domain.model.common.Image

@Composable
fun ImageList(
    imageList: List<Image>,
    title: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(items = imageList) {
                ImageItem(
                    it,
                    Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ImageItem(image: Image, modifier: Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(image.ratio)
            .width(300.dp)
        ,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.baseUrl)
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}