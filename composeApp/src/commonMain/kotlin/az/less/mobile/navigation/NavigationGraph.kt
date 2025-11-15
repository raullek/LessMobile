package az.less.mobile.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            if (showBottomBar.value) {
                AppBottomNavigation(
                    modifier = Modifier,
                    navController = navController
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeScreens.Offers.route,
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
//            authorizationGraph(navController)
            homeGraph(navController)
        }
    }
}