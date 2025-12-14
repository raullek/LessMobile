package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.less.mobile.presentation.merchant.add.MerchAddScreen
import az.less.mobile.presentation.merchant.history.MerchHistoryScreen
import az.less.mobile.presentation.merchant.more.MerchMoreScreen
import az.less.mobile.presentation.merchant.orders.MerchOrdersScreen

/**
 * Merchant flow screens
 */

sealed class MerchantScreens(val route: String) {
    data object More : MerchantScreens("merchantMore")
    data object Orders : MerchantScreens("merchantOrders")
    data object Add : MerchantScreens("merhantAdd")
    data object History : MerchantScreens("merchantHistory")
}

/**
 * Merchant navigation graph
 * Contains all merchant-related screens (MerchMore, etc.)
 */
fun NavGraphBuilder.merchantGraph(
    rootNavController: NavController,
    navController: NavController
) {
        composable(MerchantScreens.More.route) {
            MerchMoreScreen(navController = navController, navigateToClientFlow = {
                rootNavController.navigate(ROOT_CLIENT)
            })
        }
        composable(MerchantScreens.Orders.route) {
            MerchOrdersScreen(navController = navController)
        }
        composable(MerchantScreens.Add.route) {
            MerchAddScreen(navController = navController)
        }
        composable(MerchantScreens.History.route) {
            MerchHistoryScreen(navController = navController)
        }
}


val merchantHomeRoutes = listOf(
    MerchantScreens.More.route,
    MerchantScreens.Orders.route,
    MerchantScreens.Add.route,
    MerchantScreens.History.route,
)
