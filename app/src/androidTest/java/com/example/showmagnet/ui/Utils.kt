package com.example.showmagnet.ui

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag

fun ComposeContentTestRule.assertTextFeildIsVariableAndHasValueWithTage(
    tage: String,
    value: String
) {
    this.onNodeWithTag(tage).assertIsDisplayed()
    this.onNodeWithTag(tage).assert(hasText(value))
}