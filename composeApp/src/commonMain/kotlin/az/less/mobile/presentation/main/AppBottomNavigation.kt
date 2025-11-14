package az.less.mobile.presentation.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun AppBottomNavigation(
    modifier: Modifier = Modifier.background(Color.Transparent),
    navController: NavController
) {
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    // Update selected item based on current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Sync selectedItem with current route
//    bottomNavItems.forEachIndexed { index, item ->
//        if (item.route == currentRoute) {
//            selectedItem = index
//        }
//    }

//    Surface(
//        modifier = modifier
//            .height(64.dp)
//            .shadow(
//                elevation = 4.dp,
//                spotColor = AppTheme.colors.background
//            ),
//        color = AppTheme.colors.background
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 24.dp),
//
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            bottomNavItems.forEachIndexed { index, barItem ->
//                val isSelected = selectedItem == index
//                val animatedWeight = animateFloatAsState(
//                    targetValue = if (isSelected) 1.5f else 1f,
//                    animationSpec = tween(durationMillis = 400)
//                )
//                BottomNavItem(
//                    modifier = Modifier.weight(animatedWeight.value),
//                    barItem,
//                    isSelected = isSelected,
//                    onClick = {
//                        // Only navigate if we're not already on this route
//                        if (currentRoute != barItem.route) {
//                            navController.navigate(barItem.route) {
//                                // Clear the entire backstack and make this the new root
//                                popUpTo(navController.graph.findStartDestination().id) {
//                                    saveState = true
//                                }
//                                // Prevent multiple copies of the same destination
//                                launchSingleTop = true
//                                // Restore state when reselecting a previously selected item
//                                restoreState = true
//                            }
//                        }
//                    }
//                )
//            }
//        }
//    }
}

@Composable
private fun BottomNavItem(
    modifier: Modifier,
    item: BottomNavItemData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedTextState = animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

//    Row(
//        modifier = modifier
//            .height(48.dp)
//            .background(
//                color = if (isSelected) AppTheme.colors.secondary.copy(alpha = 0.2f) else Color.Transparent,
//                shape = RoundedCornerShape(12)
//            )
//            .clickable { onClick() },
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//
//        ) {
//        Icon(
//            painter = painterResource(resource = item.icon),
//            contentDescription = item.label,
//            tint = if (isSelected) AppTheme.colors.secondary else AppTheme.colors.textSecondary,
//            modifier = Modifier.size(24.dp)
//        )
//        if (isSelected) {
//            Text(
//                modifier = Modifier.padding(start = AppTheme.spacing.Medium).alpha(animatedTextState.value),
//                text = item.label,
//                style = AppTheme.typography.LabelSmall,
//                color = AppTheme.colors.primary
//            )
//        }
//    }

}


//
private data class BottomNavItemData(
    val icon: DrawableResource,
    val label: String,
    val route: String
)

//
//private val bottomNavItems = listOf(
//    BottomNavItemData(Res.drawable.ic_home_24, "Home", HomeScreens.Home.route),
//    BottomNavItemData(Res.drawable.ic_calendar_24, "Schedule", HomeScreens.Schedule.route),
//    BottomNavItemData(Res.drawable.ic_message_24, "Message", HomeScreens.Message.route),
//    BottomNavItemData(Res.drawable.ic_profile_24, "Profile", HomeScreens.Profile.route)
//)
