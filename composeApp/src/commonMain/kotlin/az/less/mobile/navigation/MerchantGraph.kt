package az.less.mobile.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import az.less.mobile.domain.model.auth.AppMode
import az.less.mobile.domain.repository.SessionLocalRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
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
import az.less.mobile.presentation.partner.preview.VenuePreviewScreen
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
    data class EditProfile(val venueData: String? = null) : MerchantRoute

    @Serializable
    data object BranchVerification : MerchantRoute

    @Serializable
    data class BranchUsers(
        val venueId: String = "",
        val venueName: String = ""
    ) : MerchantRoute

    @Serializable
    data class AddBranchUser(
        val venueId: String,
        val venueName: String,
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

    @Serializable
    data class VenuePreview(val venueId: String) : MerchantRoute
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
        val sessionLocalRepository: SessionLocalRepository = koinInject()
        val coroutineScope = rememberCoroutineScope()
        MerchMoreScreen(
            navController = navController,
            navigateToClientFlow = {
                coroutineScope.launch {
                    sessionLocalRepository.saveLastUsedMode(AppMode.CLIENT)
                }
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
            venueData = args.venueData,
            navController = navController
        )
    }

    composable<MerchantRoute.BranchVerification> {
        BranchVerificationScreen(
            navController = navController,
            onAddUsersClicked = {
                navController.navigate(MerchantRoute.BranchUsers())
            },
            onHomeClicked = {
                navController.navigate(MerchantRoute.More) {
                    popUpTo<MerchantRoute.Orders> { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }

    composable<MerchantRoute.BranchUsers> { backStackEntry ->
        val args = backStackEntry.toRoute<MerchantRoute.BranchUsers>()
        BranchUsersScreen(
            venueId = args.venueId,
            venueName = args.venueName,
            navController = navController
        )
    }

    composable<MerchantRoute.AddBranchUser> {
        AddBranchUserScreen(navController = navController, rootNavController = rootNavController)
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

    composable<MerchantRoute.VenuePreview> { backStackEntry ->
        val args = backStackEntry.toRoute<MerchantRoute.VenuePreview>()
        VenuePreviewScreen(
            venueId = args.venueId,
            navController = navController
        )
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
