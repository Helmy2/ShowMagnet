package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ChipList(
    title: String,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    items: List<String>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            items(items.size) {
                Card(modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .clickable {
                        onSelectedIndexChange(it)
                    }
                    .padding(horizontal = 8.dp), colors = CardDefaults.cardColors(
                    containerColor = if (selectedIndex == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (selectedIndex == it) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                ), border = CardDefaults.outlinedCardBorder()) {
                    Text(
                        text = items[it],
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            vertical = 4.dp, horizontal = 16.dp
                        )
                    )
                }
            }
        }
    }
}