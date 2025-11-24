package az.less.mobile.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.main.AppBottomNavigation

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var showBottomBar = rememberSaveable { mutableStateOf(false) }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    showBottomBar.value = homeRoutes.contains(currentRoute)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
    ) {
        // Navigation content
        NavHost(
            navController = navController,
            startDestination = HomeScreens.Offers.route,
            modifier = Modifier.fillMaxSize()
        ) {
//            authorizationGraph(navController)
            homeGraph(navController)
        }
        
        // Bottom Navigation Bar - positioned at bottom
        if (showBottomBar.value) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxSize()
            ) {
                AppBottomNavigation(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    navController = navController
                )
            }
        }
    }
}