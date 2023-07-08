package com.example.showmagnet.ui.common.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.showmagnet.ui.common.utils.TestTage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectedAndLoadingFeild(
    connected: Boolean,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (connected) {
        Crossfade(
            loading,
            label = "",
            modifier = Modifier,
        ) {
            if (it) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag(TestTage.LOADING)
                    )
                }
            } else {
                content()
            }
        }
    } else {
        Crossfade(
            loading,
            label = "",
            modifier = Modifier,
        ) {
            if (it) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag(TestTage.LOADING)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No Internet Connection",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        onClick = onRefresh,
                    ) {
                        Text(text = "Refresh", Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}