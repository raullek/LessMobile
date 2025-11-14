package az.less.mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.less.mobile.presentation.main.home.HomeScreen
//import org.example.project.presentation.main.home.HomeScreen
//import org.example.project.presentation.main.home.HomeViewModel
//import org.example.project.presentation.main.schedule.ScheduleScreen
//import org.example.project.presentation.main.screens.MessageScreen
//import org.example.project.presentation.main.screens.ProfileScreen
import org.koin.compose.viewmodel.koinViewModel

sealed class HomeScreens(val route: String) {
    data object Home : HomeScreens("home")
    data object Schedule : HomeScreens("schedule")
    data object Message : HomeScreens("message")
    data object Profile : HomeScreens("profile")
}

fun NavGraphBuilder.homeGraph(
    navController: NavController
) {

    composable(HomeScreens.Home.route) {
        HomeScreen()
    }
//    composable(HomeScreens.Schedule.route) {
//        ScheduleScreen()
//    }
//    composable(HomeScreens.Message.route) {
//        MessageScreen()
//    }
//    composable(HomeScreens.Profile.route) {
//        ProfileScreen()
//    }
}

val homeRoutes = listOf(
    HomeScreens.Home.route,
    HomeScreens.Schedule.route,
    HomeScreens.Message.route,
    HomeScreens.Profile.route
)