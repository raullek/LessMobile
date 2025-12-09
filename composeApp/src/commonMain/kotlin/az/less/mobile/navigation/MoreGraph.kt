package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.less.mobile.presentation.account.account.AccountScreen
import az.less.mobile.presentation.account.paymentmethods.PaymentMethodsScreen
import az.less.mobile.presentation.main.more.paymentmethods.addnewcard.AddNewCardScreen
import az.less.mobile.presentation.onboarding.welcome.WelcomeScreenContent
import az.less.mobile.presentation.onboarding.loginemail.LoginEmailScreen
import az.less.mobile.presentation.onboarding.otp.LoginCodeScreen
import az.less.mobile.presentation.onboarding.welcome.WelcomeScreen

/**
 * More flow screens
 */
sealed class MoreScreens(val route: String) {
    data object Account : MoreScreens("account")
    data object PaymentMethods : MoreScreens("paymentMethods")
    data object AddNewCard : MoreScreens("addNewCard")
    data object Welcome : MoreScreens("welcome")
    data object LoginEmail : MoreScreens("loginEmail")
    data object LoginCode : MoreScreens("loginCode")
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

