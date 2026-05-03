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
import az.less.mobile.presentation.partner.history.IncomeHistoryScreen
import az.less.mobile.presentation.partner.more.PartnerMoreScreen
import az.less.mobile.presentation.partner.places.PartnerPlacesScreen
import az.less.mobile.presentation.partner.places.edit.BranchVerificationScreen
import az.less.mobile.presentation.partner.places.edit.EditMerchantProfileScreen
import az.less.mobile.presentation.partner.places.edit.branchusers.BranchUsersScreen
import az.less.mobile.presentation.partner.places.edit.branchusers.addbranchuser.AddBranchUserScreen
import az.less.mobile.presentation.partner.places.edit.selectlocation.InputAddressScreen
import az.less.mobile.presentation.partner.places.edit.selectlocation.SelectBranchLocationOnMapScreen
import az.less.mobile.presentation.partner.preview.VenuePreviewScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

/**
 * Partner flow screens - type-safe navigation routes.
 *
 * Partner mode is used for users with roles = [client, partner] (without merchant).
 * Users with [client, merchant, partner] stay on MerchantGraph with partner-specific
 * cells surfaced by [az.less.mobile.domain.model.auth.User.isPartner].
 */
sealed interface PartnerRoute {

    // Bottom navigation routes
    @Serializable
    data object More : PartnerRoute

    @Serializable
    data object Places : PartnerRoute

    @Serializable
    data object History : PartnerRoute

    // Places flow
    @Serializable
    data class EditProfile(val venueData: String? = null) : PartnerRoute

    @Serializable
    data class BranchVerification(
        val venueId: String = "",
        val venueName: String = ""
    ) : PartnerRoute

    @Serializable
    data class BranchUsers(
        val venueId: String = "",
        val venueName: String = ""
    ) : PartnerRoute

    @Serializable
    data class AddBranchUser(
        val venueId: String,
        val venueName: String,
        val userNumber: Int,
        val user: String? = null
    ) : PartnerRoute

    // Location selection flow
    @Serializable
    data class SelectBranchLocation(
        val latitude: Double? = null,
        val longitude: Double? = null,
        val address: String? = null
    ) : PartnerRoute

    @Serializable
    data object InputAddress : PartnerRoute

    @Serializable
    data class VenuePreview(val venueId: String) : PartnerRoute
}

/**
 * Partner navigation graph.
 */
fun NavGraphBuilder.partnerGraph(
    rootNavController: NavController,
    navController: NavController
) {
    composable<PartnerRoute.More> {
        val sessionLocalRepository: SessionLocalRepository = koinInject()
        val coroutineScope = rememberCoroutineScope()
        PartnerMoreScreen(
            navController = navController,
            navigateToClientFlow = {
                coroutineScope.launch {
                    sessionLocalRepository.saveLastUsedMode(AppMode.CLIENT)
                }
                rootNavController.navigate(ROOT_CLIENT)
            }
        )
    }

    composable<PartnerRoute.Places> {
        PartnerPlacesScreen(navController = navController)
    }

    composable<PartnerRoute.History> {
        IncomeHistoryScreen(navController = navController)
    }

    composable<PartnerRoute.EditProfile> { backStackEntry ->
        val args = backStackEntry.toRoute<PartnerRoute.EditProfile>()
        EditMerchantProfileScreen(
            venueData = args.venueData,
            navController = navController,
            rootNavController = rootNavController
        )
    }

    composable<PartnerRoute.BranchVerification> { backStackEntry ->
        val args = backStackEntry.toRoute<PartnerRoute.BranchVerification>()
        BranchVerificationScreen(
            navController = navController,
            onAddUsersClicked = {
                navController.navigate(
                    PartnerRoute.BranchUsers(
                        venueId = args.venueId,
                        venueName = args.venueName
                    )
                )
            },
            onHomeClicked = {
                navController.navigate(PartnerRoute.More) {
                    popUpTo<PartnerRoute.Places> { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }

    composable<PartnerRoute.BranchUsers> { backStackEntry ->
        val args = backStackEntry.toRoute<PartnerRoute.BranchUsers>()
        BranchUsersScreen(
            venueId = args.venueId,
            venueName = args.venueName,
            navController = navController
        )
    }

    composable<PartnerRoute.AddBranchUser> {
        AddBranchUserScreen(navController = navController, rootNavController = rootNavController)
    }

    composable<PartnerRoute.SelectBranchLocation> { backStackEntry ->
        val args = backStackEntry.toRoute<PartnerRoute.SelectBranchLocation>()
        SelectBranchLocationOnMapScreen(
            initialLatitude = args.latitude,
            initialLongitude = args.longitude,
            initialAddress = args.address,
            navController = navController,
        )
    }

    composable<PartnerRoute.InputAddress> {
        InputAddressScreen(navController = navController)
    }

    composable<PartnerRoute.VenuePreview> { backStackEntry ->
        val args = backStackEntry.toRoute<PartnerRoute.VenuePreview>()
        VenuePreviewScreen(
            venueId = args.venueId,
            navController = navController
        )
    }
}

/**
 * Home routes for bottom navigation visibility check.
 */
val partnerHomeRoutes: List<KClass<out PartnerRoute>> = listOf(
    PartnerRoute.More::class,
    PartnerRoute.Places::class,
    PartnerRoute.History::class,
)
