package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import az.less.mobile.presentation.client.account.account.AccountScreen
import az.less.mobile.presentation.client.account.paymentmethods.PaymentMethodsScreen
import az.less.mobile.presentation.client.main.categoryoffers.CategoryOffersScreen
import az.less.mobile.presentation.client.main.explore.ExploreScreen
import az.less.mobile.presentation.client.main.merchant.MerchantProfileScreen
import az.less.mobile.presentation.client.main.more.root.MoreScreen
import az.less.mobile.presentation.client.main.offers.OffersScreen
import az.less.mobile.presentation.client.main.orders.OrdersScreen
import az.less.mobile.presentation.client.main.favorites.FavoritesScreen
import az.less.mobile.presentation.client.main.search.SearchScreen
import az.less.mobile.presentation.client.onboarding.loginemail.LoginEmailScreen
import az.less.mobile.presentation.client.onboarding.otp.LoginCodeScreen
import az.less.mobile.presentation.client.onboarding.welcome.WelcomeScreen
import az.less.mobile.presentation.client.reserve.OrderAcceptedScreen
import az.less.mobile.presentation.client.reserve.models.OrderAccepted
import az.less.mobile.presentation.client.main.voucher.VoucherScreen
import az.less.mobile.presentation.main.more.paymentmethods.addnewcard.AddNewCardScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

/**
 * Client flow screens - Type-safe navigation routes
 */
sealed interface ClientRoute {

    // Bottom navigation routes (Home screens)
    @Serializable
    data object Offers : ClientRoute

    @Serializable
    data object Explore : ClientRoute

    @Serializable
    data object Orders : ClientRoute

    @Serializable
    data object Favorites : ClientRoute

    @Serializable
    data object More : ClientRoute

    @Serializable
    data object Search : ClientRoute

    @Serializable
    data class CategoryOffers(
        val categoryId: String,
        val categoryType: String,  // Type from backend (e.g., "FOOD_CATEGORY", "DISCOUNT", "NEAREST")
        val categoryTitle: String
    ) : ClientRoute

    @Serializable
    data class OfferDetail(val offerId: String) : ClientRoute

    @Serializable
    data class Merchant(val merchantId: String) : ClientRoute

    /**
     * Reserve Flow
     */
    @Serializable
    data class OrderAccepted(
        val orderNumber: String,
        val venueName: String,
        val pickupTime: String
    ) : ClientRoute

    // More graph screens
    @Serializable
    data object Account : ClientRoute

    @Serializable
    data object PaymentMethods : ClientRoute

    @Serializable
    data object AddNewCard : ClientRoute

    @Serializable
    data object Voucher : ClientRoute

    @Serializable
    data object Welcome : ClientRoute

    @Serializable
    data object LoginEmail : ClientRoute

    @Serializable
    data class LoginCode(val email: String) : ClientRoute
}

/**
 * Main navigation graph (Home screens)
 * Contains all main client-related screens
 */
fun NavGraphBuilder.mainGraph(
    rootNavController: NavController,
    navController: NavController
) {
    composable<ClientRoute.Offers> {
        OffersScreen(navController = navController)
    }

    composable<ClientRoute.Explore> {
        ExploreScreen()
    }

    composable<ClientRoute.Orders> {
        OrdersScreen(navController = navController)
    }

    composable<ClientRoute.Favorites> {
        FavoritesScreen(navController = navController)
    }

    composable<ClientRoute.More> {
        MoreScreen(
            navController = navController,
            navigateToMerchant = { rootNavController.navigate(ROOT_MERCHANT) }
        )
    }

    composable<ClientRoute.Search> {
        SearchScreen(navController = navController)
    }

    composable<ClientRoute.CategoryOffers> { backStackEntry ->
        val args = backStackEntry.toRoute<ClientRoute.CategoryOffers>()
        CategoryOffersScreen(
            navController = navController,
            categoryId = args.categoryId,
            categoryType = args.categoryType,
            categoryTitle = args.categoryTitle
        )
    }

    composable<ClientRoute.Merchant> {
        MerchantProfileScreen(navController = navController)
    }

    composable<ClientRoute.OrderAccepted> { backStackEntry ->
        val args = backStackEntry.toRoute<ClientRoute.OrderAccepted>()
        OrderAcceptedScreen(
            navController = navController,
            orderInfo = OrderAccepted(
                orderNumber = args.orderNumber,
                venueName = args.venueName,
                pickupTime = args.pickupTime
            )
        )
    }
}

/**
 * More navigation graph
 * Contains all account and settings related screens
 */
fun NavGraphBuilder.moreGraph(
    rootNavController: NavController,
    navController: NavController
) {
    composable<ClientRoute.PaymentMethods> {
        PaymentMethodsScreen(navController = navController)
    }

    composable<ClientRoute.Account> {
        AccountScreen(navController = navController)
    }

    composable<ClientRoute.AddNewCard> {
        AddNewCardScreen(navController = navController)
    }

    composable<ClientRoute.Voucher> {
        VoucherScreen(navController = navController)
    }

    composable<ClientRoute.Welcome> {
        WelcomeScreen(navController = navController)
    }

    composable<ClientRoute.LoginEmail> {
        LoginEmailScreen(navController = navController)
    }

    composable<ClientRoute.LoginCode> { backStackEntry ->
        val args = backStackEntry.toRoute<ClientRoute.LoginCode>()
        LoginCodeScreen(
            navController = navController,
            email = args.email,
            navigateToMerchant = { rootNavController.navigate(ROOT_MERCHANT) }
        )
    }
}

/**
 * Home routes for bottom navigation visibility check
 */
val clientHomeRoutes: List<KClass<out ClientRoute>> = listOf(
    ClientRoute.Offers::class,
    ClientRoute.Explore::class,
    ClientRoute.Orders::class,
    ClientRoute.Favorites::class,
    ClientRoute.More::class
)
