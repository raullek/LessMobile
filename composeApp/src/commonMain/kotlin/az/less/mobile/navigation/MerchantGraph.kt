package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.savedstate.read
import az.less.mobile.presentation.merchant.add.MerchAddScreen
import az.less.mobile.presentation.merchant.editmerchantprofile.BranchVerificationScreen
import az.less.mobile.presentation.merchant.editmerchantprofile.branchusers.BranchUsersScreen
import az.less.mobile.presentation.merchant.editmerchantprofile.branchusers.addbranchuser.AddBranchUserScreen
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
    data object BranchVerification : MerchantScreens("branchVerification")
    data object BranchUsers : MerchantScreens("branchUsers")
    data object AddBranchUser : MerchantScreens("addBranchUser/{userNumber}?user={user}") {
        /** Create route for adding new user */
        fun createRoute(userNumber: Int) = "addBranchUser/$userNumber"
        /** Create route for editing existing user with serialized user data */
        fun createEditRoute(userNumber: Int, userJson: String) = "addBranchUser/$userNumber?user=$userJson"
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
}


val merchantHomeRoutes = listOf(
    MerchantScreens.More.route,
    MerchantScreens.Orders.route,
    MerchantScreens.Add.route,
    MerchantScreens.History.route,
)
