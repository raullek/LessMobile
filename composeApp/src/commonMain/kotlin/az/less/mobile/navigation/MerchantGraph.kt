package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import az.less.mobile.presentation.merchant.add.MerchAddScreen
import az.less.mobile.presentation.merchant.history.MerchHistoryScreen
import az.less.mobile.presentation.merchant.more.MerchMoreScreen
import az.less.mobile.presentation.merchant.orders.MerchOrdersScreen
import az.less.mobile.presentation.merchant.places.MerchPlacesScreen
import az.less.mobile.presentation.merchant.places.edit.EditMerchantProfileScreen

/**
 * Merchant flow screens
 */

sealed class MerchantScreens(val route: String) {
    data object More : MerchantScreens("merchantMore")
    data object Orders : MerchantScreens("merchantOrders")
    data object Add : MerchantScreens("merhantAdd")
    data object History : MerchantScreens("merchantHistory")
    data object Places : MerchantScreens("merchantPlaces")
    data object AddBranch : MerchantScreens("merchantAddBranch")
    data class EditMerchantProfile(val branchId: String) : MerchantScreens("merchantEditMerchantProfile/{branchId}") {
        companion object {
            fun createRoute(branchId: String?) = if (branchId != null) "merchantEditMerchantProfile/$branchId" else "merchantAddBranch"
        }
    }
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
        composable(MerchantScreens.Places.route) {
            MerchPlacesScreen(navController = navController)
        }
        composable(MerchantScreens.AddBranch.route) {
            EditMerchantProfileScreen(
                branchId = null,
                navController = navController
            )
        }
        composable(
            route = MerchantScreens.EditMerchantProfile("").route,
            arguments = listOf(navArgument("branchId") { type = NavType.StringType })
        ) { backStackEntry ->
            val branchId = backStackEntry.arguments?.getString("branchId")
            EditMerchantProfileScreen(
                branchId = branchId,
                navController = navController
            )
        }
}


val merchantHomeRoutes = listOf(
    MerchantScreens.More.route,
    MerchantScreens.Orders.route,
    MerchantScreens.Add.route,
    MerchantScreens.History.route,
)
