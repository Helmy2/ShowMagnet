package com.example.showmagnet.ui.common.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChoiceField(
    selectedIndex: Int,
    selectionList: List<String>,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .border(
                    2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp)
                ),
        ) {
            for (it in selectionList.indices) {
                val color by animateColorAsState(
                    targetValue = if (it == selectedIndex) MaterialTheme.colorScheme.secondary
                    else Color.Transparent, label = ""
                )

                val textColor by animateColorAsState(
                    targetValue = if (it == selectedIndex) MaterialTheme.colorScheme.onSecondary
                    else MaterialTheme.colorScheme.onBackground, label = ""
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(color)
                        .clickable { onItemClicked(it) },
                ) {
                    Text(
                        text = selectionList[it],
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = textColor
                    )
                }
            }
        }
    }
}