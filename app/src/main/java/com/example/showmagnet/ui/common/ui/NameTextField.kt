package com.example.showmagnet.ui.common.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NameTextField(
    name: String, onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseTextFiled(
        modifier = modifier,
        value = name,
        onValueChange = onValueChange,
        label = {
            Text(
                "Name",
                style = MaterialTheme.typography.labelMedium,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
        )
    )
}