package com.example.showmagnet.ui.navigation.component

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.showmagnet.ui.navigation.AppDestinations
import com.example.showmagnet.ui.onboarding.OnboardingScreen

fun NavGraphBuilder.onboardingScreen(
    onNavigateToSignIn: () -> Unit
) {
    composable(AppDestinations.Onboarding.route) {
        OnboardingScreen(onEndOnboarding = onNavigateToSignIn)
    }
}