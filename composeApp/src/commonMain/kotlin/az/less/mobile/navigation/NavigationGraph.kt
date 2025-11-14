package az.less.mobile.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import az.less.mobile.presentation.main.AppBottomNavigation

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var showBottomBar = rememberSaveable { mutableStateOf(false) }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    showBottomBar.value = homeRoutes.contains(currentRoute)

    Scaffold(bottomBar = {
        if (showBottomBar.value) {
            AppBottomNavigation(
                modifier = Modifier,
                navController = navController
            )
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = HomeScreens.Home.route
        ) {
//            authorizationGraph(navController)
            homeGraph(navController)
        }

    }
}