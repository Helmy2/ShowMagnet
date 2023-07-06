package com.example.showmagnet.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable { onItemClick() },
    ) {
        Text(
            text = text, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    vertical = 4.dp, horizontal = 16.dp
                )
        )
    }
}