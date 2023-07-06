package com.example.showmagnet.ui.onboarding

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.showmagnet.ui.common.theme.ShowMagnetTheme
import org.junit.Rule
import org.junit.Test

class OnboardingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun firstOnboardingScreen() {
        composeTestRule.setContent {
            ShowMagnetTheme {
                OnboardingScreen(
                    onEndOnboarding = {}
                )
            }
        }

        val item = OnBoardingItems.getData().first()
        composeTestRule.onNodeWithText(item.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.subTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText("Next").assertIsDisplayed()
    }

    @Test
    fun onNextButtonNavigateThePager() {
        var wasCalled = false
        composeTestRule.setContent {
            ShowMagnetTheme { OnboardingScreen(onEndOnboarding = { wasCalled = true }) }
        }

        val items = OnBoardingItems.getData()

        for ((i, item) in items.withIndex()) {
            composeTestRule.onNodeWithText(item.title).assertIsDisplayed()
            composeTestRule.onNodeWithText(item.subTitle).assertIsDisplayed()

            val buttonText = if (i == items.lastIndex) "Start" else "Next"
            composeTestRule.onNodeWithText(buttonText).assertIsDisplayed()
            composeTestRule.onNodeWithText(buttonText).performClick()
        }
        assert(wasCalled)
    }
}