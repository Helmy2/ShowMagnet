package com.example.showmagnet.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            Text(text = "HomeScreen")
        }
    }
}
