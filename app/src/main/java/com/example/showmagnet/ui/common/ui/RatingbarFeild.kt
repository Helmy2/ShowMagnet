package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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