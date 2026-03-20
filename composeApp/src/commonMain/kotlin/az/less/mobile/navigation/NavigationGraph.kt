package az.less.mobile.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import az.less.designsystem.base.LessTheme
import az.less.mobile.domain.model.auth.AppMode
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.presentation.client.AppClientBottomNavigation
import az.less.mobile.presentation.merchant.AppMerchantBottomNavigation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private fun logNavigation(tag: String, route: String) {
    println("[$tag] -> $route")
}


const val ROOT_CLIENT = "rootClientNavigation"
const val ROOT_MERCHANT = "rootMerchantNavigation"



@Composable
fun AppRootNavigation() {
    val sessionLocalRepository: SessionLocalRepository = koinInject()
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        val user = sessionLocalRepository.currentUser.first()
        val lastMode = sessionLocalRepository.lastUsedMode.first()
        if (user != null && lastMode == AppMode.MERCHANT && AppMode.MERCHANT in user.availableModes()) {
            navController.navigate(ROOT_MERCHANT) {
                popUpTo(ROOT_CLIENT) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = ROOT_CLIENT
    ) {
        clientGraph(navController)
        merchantGraph(navController)
    }
}

@Composable
fun AppClientRootScreen(rootNavController: NavController) {
    val navController = rememberNavController()
    var showBottomBar = rememberSaveable { mutableStateOf(false) }

    // Navigation logging
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            logNavigation("CLIENT", destination.route ?: "unknown")
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    showBottomBar.value = clientHomeRoutes.any { currentRoute?.contains(it.simpleName ?: "") == true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
    ) {
        // Navigation content
        NavHost(
            navController = navController,
            startDestination = ClientRoute.Offers,
            modifier = Modifier.fillMaxSize()
        ) {
            mainGraph(rootNavController = rootNavController, navController = navController)
            moreGraph(rootNavController = rootNavController, navController = navController)
        }

        // Bottom Navigation Bar - positioned at bottom
        if (showBottomBar.value) {
            AppClientBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter),
                navController = navController
            )
        }
    }
}

@Composable
fun AppMerchantRootScreen(rootNavController: NavController) {
    val navController = rememberNavController()
    var showBottomBar = rememberSaveable { mutableStateOf(false) }

    // Navigation logging
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            logNavigation("MERCHANT", destination.route ?: "unknown")
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    showBottomBar.value = merchantHomeRoutes.any { currentRoute?.contains(it.simpleName ?: "") == true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
    ) {
        // Navigation content
        NavHost(
            navController = navController,
            startDestination = MerchantRoute.Orders,
            modifier = Modifier.fillMaxSize()
        ) {
         //   clientGraph(navController)
            merchantGraph(rootNavController = rootNavController, navController = navController)
        }

        // Bottom Navigation Bar - positioned at bottom
        if (showBottomBar.value) {
            AppMerchantBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter),
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.clientGraph(
    navController: NavController
){
    composable (ROOT_CLIENT){
        AppClientRootScreen(navController)
    }
}

fun NavGraphBuilder.merchantGraph(
    navController: NavController
){
    composable (ROOT_MERCHANT){
        AppMerchantRootScreen(navController)
    }
}