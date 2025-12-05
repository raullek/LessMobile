package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import az.less.mobile.presentation.reserve.OrderAcceptedScreen
import az.less.mobile.presentation.reserve.models.OrderAccepted

/**
 * Reserve flow screens
 */
sealed class ReserveScreens(val route: String) {
    data object OrderAccepted : ReserveScreens("order_accepted/{orderNumber}/{venueName}/{pickupTime}") {
        fun createRoute(orderNumber: String, venueName: String, pickupTime: String): String {
            return "order_accepted/$orderNumber/$venueName/$pickupTime"
        }
    }
}

/**
 * Reserve navigation graph
 * Contains all reserve-related screens (order confirmation, etc.)
 */
fun NavGraphBuilder.reserveGraph(
    navController: NavController
) {
    composable(
        route = ReserveScreens.OrderAccepted.route,
        arguments = listOf(
            navArgument("orderNumber") { type = NavType.StringType },
            navArgument("venueName") { type = NavType.StringType },
            navArgument("pickupTime") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val orderNumber = backStackEntry.arguments?.getString("orderNumber") ?: ""
        val venueName = backStackEntry.arguments?.getString("venueName") ?: ""
        val pickupTime = backStackEntry.arguments?.getString("pickupTime") ?: ""
        
        OrderAcceptedScreen(
            navController = navController,
            orderInfo = OrderAccepted(
                orderNumber = orderNumber,
                venueName = venueName,
                pickupTime = pickupTime
            )
        )
    }
}


