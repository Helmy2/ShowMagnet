package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.showmagnet.ui.common.theme.ShowMagnetTheme

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
        )
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Default.ArrowForwardIos,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingItemPreview() {
    ShowMagnetTheme {
        SettingItem(Icons.Default.Logout, "Logout") {}
    }
}