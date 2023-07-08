package com.example.showmagnet.ui.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.showmagnet.ui.common.utils.TestTage

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        if (loading)
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.testTag(TestTage.LOADING)
            )
        else
            SignButton(
                enabled = enabled,
                onClick = onClick,
                content = content,
            )
    }
}