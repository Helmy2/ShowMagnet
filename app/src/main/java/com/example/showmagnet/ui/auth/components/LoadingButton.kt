package com.example.showmagnet.ui.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingButton(
    enabled: Boolean,
    loading: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        if (loading)
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onBackground
            )
        else
            SignButton(
                enabled = enabled,
                onClick = onClick,
                content = content,
            )
    }
}