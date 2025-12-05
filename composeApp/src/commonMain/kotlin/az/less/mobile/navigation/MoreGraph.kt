package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.less.mobile.presentation.account.account.AccountScreen
import az.less.mobile.presentation.account.paymentmethods.PaymentMethodsScreen
import az.less.mobile.presentation.main.more.paymentmethods.addnewcard.AddNewCardScreen

/**
 * More flow screens
 */
sealed class MoreScreens(val route: String) {
    data object Account : MoreScreens("account")
    data object PaymentMethods : MoreScreens("paymentMethods")
    data object AddNewCard : MoreScreens("addNewCard")
}

/**
 * More navigation graph
 * Contains all more-related screens (Account, PaymentMethods, AddNewCard, etc.)
 */
fun NavGraphBuilder.moreGraph(
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
}

