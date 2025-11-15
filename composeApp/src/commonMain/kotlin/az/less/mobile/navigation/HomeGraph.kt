package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.less.mobile.presentation.main.explore.ExploreScreen
import az.less.mobile.presentation.main.more.MoreScreen
import az.less.mobile.presentation.main.offers.OffersScreen
import az.less.mobile.presentation.main.orders.OrdersScreen
import az.less.mobile.presentation.main.saved.SavedScreen

sealed class HomeScreens(val route: String) {
    data object Offers : HomeScreens("offers")
    data object Explore : HomeScreens("explore")
    data object Orders : HomeScreens("orders")
    data object Saved : HomeScreens("saved")
    data object More : HomeScreens("more")
}

fun NavGraphBuilder.homeGraph(
    navController: NavController
) {
    composable(HomeScreens.Offers.route) {
        OffersScreen(navController = navController)
    }
    composable(HomeScreens.Explore.route) {
        ExploreScreen()
    }
    composable(HomeScreens.Orders.route) {
        OrdersScreen()
    }
    composable(HomeScreens.Saved.route) {
        SavedScreen(navController = navController)
    }
    composable(HomeScreens.More.route) {
        MoreScreen(navController = navController)
    }
}

val homeRoutes = listOf(
    HomeScreens.Offers.route,
    HomeScreens.Explore.route,
    HomeScreens.Orders.route,
    HomeScreens.Saved.route,
    HomeScreens.More.route
)