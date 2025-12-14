package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import az.less.mobile.presentation.client.account.account.AccountScreen
import az.less.mobile.presentation.client.account.paymentmethods.PaymentMethodsScreen
import az.less.mobile.presentation.client.main.categoryoffers.CategoryOffersScreen
import az.less.mobile.presentation.client.main.explore.ExploreScreen
import az.less.mobile.presentation.client.main.more.root.MoreScreen
import az.less.mobile.presentation.client.main.offers.OffersScreen
import az.less.mobile.presentation.client.main.orders.OrdersScreen
import az.less.mobile.presentation.client.main.saved.SavedScreen
import az.less.mobile.presentation.client.main.search.SearchScreen
import az.less.mobile.presentation.client.onboarding.loginemail.LoginEmailScreen
import az.less.mobile.presentation.client.onboarding.otp.LoginCodeScreen
import az.less.mobile.presentation.client.onboarding.welcome.WelcomeScreen
import az.less.mobile.presentation.client.reserve.OrderAcceptedScreen
import az.less.mobile.presentation.client.reserve.models.OrderAccepted
import az.less.mobile.presentation.main.more.paymentmethods.addnewcard.AddNewCardScreen




sealed class HomeScreens(val route: String) {
    data object Offers : HomeScreens("offers")
    data object Explore : HomeScreens("explore")
    data object Orders : HomeScreens("orders")
    data object Saved : HomeScreens("saved")
    data object More : HomeScreens("more")
    data object Search : HomeScreens("search")
    data object CategoryOffers : HomeScreens("category_offers")

    /**
     * Reserve Flow
     * */
    data object OrderAccepted : HomeScreens("order_accepted/{orderNumber}/{venueName}/{pickupTime}") {
        fun createRoute(orderNumber: String, venueName: String, pickupTime: String): String {
            return "order_accepted/$orderNumber/$venueName/$pickupTime"
        }
    }


}


sealed class MoreScreens(val route: String) {
    data object Account : MoreScreens("account")
    data object PaymentMethods : MoreScreens("paymentMethods")
    data object AddNewCard : MoreScreens("addNewCard")
    data object Welcome : MoreScreens("welcome")
    data object LoginEmail : MoreScreens("loginEmail")
    data object LoginCode : MoreScreens("loginCode")
}

fun NavGraphBuilder.mainGraph(
    rootNavController: NavController,
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
        MoreScreen(navController = navController, navigateToMerchant = {rootNavController.navigate(ROOT_MERCHANT)})
    }
    composable(HomeScreens.Search.route) {
        SearchScreen(navController = navController)
    }
    composable(HomeScreens.CategoryOffers.route) {
        CategoryOffersScreen(navController = navController)
    }
    composable(
        route = HomeScreens.OrderAccepted.route,
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


fun NavGraphBuilder.moreGraph(
    rootNavController: NavController,
    navController: NavController
) {
    composable(MoreScreens.PaymentMethods.route) {
        PaymentMethodsScreen(navController = navController)
    }
    composable(MoreScreens.Account.route) {
        AccountScreen(navController = navController)
    }
    composable(MoreScreens.AddNewCard.route) {
        AddNewCardScreen(navController = navController)
    }
    composable(MoreScreens.Welcome.route) {
        WelcomeScreen(navController = navController)
    }
    composable(MoreScreens.LoginEmail.route) {
        LoginEmailScreen(navController = navController)
    }
    composable(MoreScreens.LoginCode.route) {
        LoginCodeScreen(navController = navController)
    }
}

val homeRoutes = listOf(
    HomeScreens.Offers.route,
    HomeScreens.Explore.route,
    HomeScreens.Orders.route,
    HomeScreens.Saved.route,
    HomeScreens.More.route
)