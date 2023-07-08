package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

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