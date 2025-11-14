package az.less.mobile.presentation.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.HomeScreens
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_more_24dp
import lessmobile.composeapp.generated.resources.ic_offers_24dp
import lessmobile.composeapp.generated.resources.ic_orders_24dp
import lessmobile.composeapp.generated.resources.ic_saved_24dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(LessTheme.size.huge),
        color = LessTheme.colors.backgroundPrimary,
        shadowElevation = LessTheme.elevation.xSmall
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route
                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    item = item,
                    isSelected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    modifier: Modifier = Modifier,
    item: BottomNavItemData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) LessTheme.colors.textIconsBrand else LessTheme.colors.textIconsGrey,
        animationSpec = tween(durationMillis = 100)
    )

    Column(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = LessTheme.spacing.xSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(resource = item.icon),
            contentDescription = item.label,
            tint = iconColor,
            modifier = Modifier.size(LessTheme.size.medium)
        )
        Text(
            text = item.label,
            style = LessTheme.typography.caption12Medium,
            color = iconColor,
            modifier = Modifier.padding(top = LessTheme.spacing.xxSmall)
        )
    }
}

private data class BottomNavItemData(
    val icon: DrawableResource,
    val label: String,
    val route: String
)

private val bottomNavItems = listOf(
    BottomNavItemData(Res.drawable.ic_offers_24dp, "Offers", HomeScreens.Offers.route),
    BottomNavItemData(Res.drawable.ic_explore_24dp, "Explore", HomeScreens.Explore.route),
    BottomNavItemData(Res.drawable.ic_orders_24dp, "Orders", HomeScreens.Orders.route),
    BottomNavItemData(Res.drawable.ic_saved_24dp, "Saved", HomeScreens.Saved.route),
    BottomNavItemData(Res.drawable.ic_more_24dp, "More", HomeScreens.More.route)
)
