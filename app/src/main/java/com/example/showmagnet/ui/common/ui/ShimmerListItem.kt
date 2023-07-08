package com.example.showmagnet.ui.common.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShimmerListItem(
    isLoading: Boolean,
    contentAfterLoading: @Composable (Modifier) -> Unit,
    contentBeforeLoading: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Crossfade(targetState = isLoading, label = "") {
        if (it) {
            contentBeforeLoading(modifier)
        } else {
            contentAfterLoading(modifier)
        }
    }
}