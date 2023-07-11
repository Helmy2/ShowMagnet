package com.example.showmagnet.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.showmagnet.ui.navigation.AppDestinations
import com.example.showmagnet.ui.navigation.MainNavHost


data class NavigationBarElement(
    val label: String, val destinations: AppDestinations, val icon: ImageVector
)

val elementList = listOf(
    NavigationBarElement(
        "Home", AppDestinations.Home, Icons.Filled.Home
    ),
    NavigationBarElement(
        "Explore", AppDestinations.Discover, Icons.Filled.Explore
    ),
    NavigationBarElement(
        "Favorite", AppDestinations.Favorite, Icons.Filled.Favorite
    ),
    NavigationBarElement(
        "Profile", AppDestinations.Profile, Icons.Filled.Person
    ),
)

@Composable
fun MainScreen(
    onNavigateToMovie: (id: Int) -> Unit,
    onNavigateToTv: (id: Int) -> Unit,
    onNavigateToPeron: (id: Int) -> Unit,
) {
    val navController = rememberNavController()

    Scaffold(bottomBar = {
        NavigationBar() {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            elementList.forEach { element ->
                NavigationBarItem(alwaysShowLabel = false,
                    icon = { Icon(element.icon, contentDescription = element.label) },
                    selected = currentDestination?.hierarchy?.any { it.route == element.destinations.route } == true,
                    onClick = {
                        navController.navigate(element.destinations.route) {

                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    })
            }
        }
    }) { innerPadding ->
        MainNavHost(
            navController = navController,
            onNavigateToMovie = onNavigateToMovie,
            onNavigateToTv = onNavigateToTv,
            onNavigateToPeron = onNavigateToPeron,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
