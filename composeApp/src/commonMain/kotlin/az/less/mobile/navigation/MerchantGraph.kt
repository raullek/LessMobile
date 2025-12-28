package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.savedstate.read
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

/**
 * Merchant flow screens
 */

sealed class MerchantScreens(val route: String) {
    data object More : MerchantScreens("merchantMore")
    data object Orders : MerchantScreens("merchantOrders")
    data object AddLot : MerchantScreens("merchantAddLot")
    data object History : MerchantScreens("merchantHistory")
    data object Places : MerchantScreens("merchantPlaces")
    data object AddBranch : MerchantScreens("merchantAddBranch")
    data class EditMerchantProfile(val branchId: String) : MerchantScreens("merchantEditMerchantProfile/{branchId}") {
        companion object {
            fun createRoute(branchId: String?) = if (branchId != null) "merchantEditMerchantProfile/$branchId" else "merchantAddBranch"
        }
    }
    data object BranchVerification : MerchantScreens("branchVerification")
    data object BranchUsers : MerchantScreens("branchUsers")
    data object AddBranchUser : MerchantScreens("addBranchUser/{userNumber}?user={user}") {
        /** Create route for adding new user */
        fun createRoute(userNumber: Int) = "addBranchUser/$userNumber"
        /** Create route for editing existing user with serialized user data */
        fun createEditRoute(userNumber: Int, userJson: String) = "addBranchUser/$userNumber?user=$userJson"
    }
    data object SelectBranchLocation : MerchantScreens("selectBranchLocation?lat={lat}&lng={lng}") {
        fun createRoute(latitude: Double? = null, longitude: Double? = null): String {
            return if (latitude != null && longitude != null) {
                "selectBranchLocation?lat=$latitude&lng=$longitude"
            } else {
                "selectBranchLocation"
            }
        }
    }
    data object InputAddress : MerchantScreens("inputAddress")
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
        composable(MerchantScreens.AddLot.route) {
            AddLotScreen(navController = navController)
        }
        composable(MerchantScreens.History.route) {
            IncomeHistoryScreen(navController = navController)
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
            route = "merchantEditMerchantProfile/{branchId}",
            arguments = listOf(
                navArgument("branchId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val branchId = backStackEntry.arguments?.read { getString("branchId") }
            EditMerchantProfileScreen(
                branchId = branchId,
                navController = navController
            )
        }
        composable(MerchantScreens.BranchVerification.route) {
            BranchVerificationScreen(
                navController = navController,
                onAddUsersClicked = {
                    navController.navigate(MerchantScreens.BranchUsers.route)
                },
                onHomeClicked = {
                    navController.popBackStack(MerchantScreens.More.route, inclusive = false)
                }
            )
        }
        composable(MerchantScreens.BranchUsers.route) {
            BranchUsersScreen(navController = navController)
        }
        composable(
            route = MerchantScreens.AddBranchUser.route,
            arguments = listOf(
                navArgument("userNumber") { type = NavType.IntType },
                navArgument("user") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            AddBranchUserScreen(navController = navController)
        }
        composable(
            route = MerchantScreens.SelectBranchLocation.route,
            arguments = listOf(
                navArgument("lat") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("lng") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val latString = backStackEntry.arguments?.getString("lat")
            val lngString = backStackEntry.arguments?.getString("lng")
            val latitude = latString?.toDoubleOrNull()
            val longitude = lngString?.toDoubleOrNull()

            SelectBranchLocationOnMapScreen(
                initialLatitude = latitude,
                initialLongitude = longitude,
                navController = navController,
                onLocationSelected = { lat, lng, address ->
                    // Pass result back to previous screen via SavedStateHandle
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_latitude", lat)
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_longitude", lng)
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_address", address)
                }
            )
        }
        composable(MerchantScreens.InputAddress.route) {
            InputAddressScreen(
                navController = navController,
                onAddressSelected = { address, lat, lng ->
                    // Pass result back to SelectBranchLocation screen via SavedStateHandle
                    navController.previousBackStackEntry?.savedStateHandle?.set("input_address", address)
                    navController.previousBackStackEntry?.savedStateHandle?.set("input_latitude", lat)
                    navController.previousBackStackEntry?.savedStateHandle?.set("input_longitude", lng)
                }
            )
        }
}


val merchantHomeRoutes = listOf(
    MerchantScreens.More.route,
    MerchantScreens.Orders.route,
    MerchantScreens.AddLot.route,
    MerchantScreens.History.route,
)
