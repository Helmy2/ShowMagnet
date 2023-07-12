package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = text, style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(
                    vertical = 4.dp, horizontal = 16.dp
                )
        )
    }
}