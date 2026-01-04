package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import az.less.mobile.presentation.merchant.add.addlot.AddLotScreen
import az.less.mobile.presentation.merchant.history.IncomeHistoryScreen
import az.less.mobile.presentation.merchant.more.MerchMoreScreen
import az.less.mobile.presentation.merchant.orders.MerchOrdersScreen
import az.less.mobile.presentation.merchant.places.MerchPlacesScreen
import az.less.mobile.presentation.merchant.places.edit.BranchVerificationScreen
import az.less.mobile.presentation.merchant.places.edit.EditMerchantProfileScreen
import az.less.mobile.presentation.merchant.places.edit.branchusers.BranchUsersScreen
import az.less.mobile.presentation.merchant.places.edit.branchusers.addbranchuser.AddBranchUserScreen
import az.less.mobile.presentation.merchant.places.edit.selectlocation.InputAddressScreen
import az.less.mobile.presentation.merchant.places.edit.selectlocation.SelectBranchLocationOnMapScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

/**
 * Merchant flow screens - Type-safe navigation routes
 */
sealed interface MerchantRoute {

    // Bottom navigation routes
    @Serializable
    data object More : MerchantRoute

    @Serializable
    data object Orders : MerchantRoute

    @Serializable
    data object AddLot : MerchantRoute

    @Serializable
    data object History : MerchantRoute

    // Places flow
    @Serializable
    data object Places : MerchantRoute

    @Serializable
    data class EditProfile(val branchId: String? = null) : MerchantRoute

    @Serializable
    data object BranchVerification : MerchantRoute

    @Serializable
    data object BranchUsers : MerchantRoute

    @Serializable
    data class AddBranchUser(
        val userNumber: Int,
        val user: String? = null
    ) : MerchantRoute

    // Location selection flow
    @Serializable
    data class SelectBranchLocation(
        val latitude: Double? = null,
        val longitude: Double? = null,
        val address: String? = null
    ) : MerchantRoute

    @Serializable
    data object InputAddress : MerchantRoute
}

/**
 * Merchant navigation graph
 * Contains all merchant-related screens
 */
fun NavGraphBuilder.merchantGraph(
    rootNavController: NavController,
    navController: NavController
) {
    // Bottom navigation screens
    composable<MerchantRoute.More> {
        MerchMoreScreen(
            navController = navController,
            navigateToClientFlow = {
                rootNavController.navigate(ROOT_CLIENT)
            }
        )
    }

    composable<MerchantRoute.Orders> {
        MerchOrdersScreen(navController = navController)
    }

    composable<MerchantRoute.AddLot> {
        AddLotScreen(navController = navController)
    }

    composable<MerchantRoute.History> {
        IncomeHistoryScreen(navController = navController)
    }

    // Places flow screens
    composable<MerchantRoute.Places> {
        MerchPlacesScreen(navController = navController)
    }

    composable<MerchantRoute.EditProfile> { backStackEntry ->
        val args = backStackEntry.toRoute<MerchantRoute.EditProfile>()
        EditMerchantProfileScreen(
            branchId = args.branchId,
            navController = navController
        )
    }

    composable<MerchantRoute.BranchVerification> {
        BranchVerificationScreen(
            navController = navController,
            onAddUsersClicked = {
                navController.navigate(MerchantRoute.BranchUsers)
            },
            onHomeClicked = {
                navController.popBackStack<MerchantRoute.More>(inclusive = false)
            }
        )
    }

    composable<MerchantRoute.BranchUsers> {
        BranchUsersScreen(navController = navController)
    }

    composable<MerchantRoute.AddBranchUser> {
        AddBranchUserScreen(navController = navController)
    }

    // Location selection flow screens
    composable<MerchantRoute.SelectBranchLocation> { backStackEntry ->
        val args = backStackEntry.toRoute<MerchantRoute.SelectBranchLocation>()
        SelectBranchLocationOnMapScreen(
            initialLatitude = args.latitude,
            initialLongitude = args.longitude,
            initialAddress = args.address,
            navController = navController,
        )
    }

    composable<MerchantRoute.InputAddress> {
        InputAddressScreen(navController = navController)
    }
}

/**
 * Home routes for bottom navigation visibility check
 */
val merchantHomeRoutes: List<KClass<out MerchantRoute>> = listOf(
    MerchantRoute.More::class,
    MerchantRoute.Orders::class,
    MerchantRoute.AddLot::class,
    MerchantRoute.History::class,
)
