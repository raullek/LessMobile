package az.less.mobile.presentation.client

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.ClientRoute
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_more_24dp
import lessmobile.composeapp.generated.resources.ic_offers_24dp
import lessmobile.composeapp.generated.resources.ic_orders_24dp
import lessmobile.composeapp.generated.resources.ic_saved_24dp
import lessmobile.composeapp.generated.resources.nav_offers
import lessmobile.composeapp.generated.resources.nav_explore
import lessmobile.composeapp.generated.resources.nav_orders
import lessmobile.composeapp.generated.resources.nav_saved
import lessmobile.composeapp.generated.resources.nav_more
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppClientBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = LessTheme.spacing.medium)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(100.dp),
                ambientColor = Color.Black.copy(alpha = 0.5f),
                spotColor = Color.Black.copy(alpha = 0.4f)
            ).background(Color.Transparent)
        ,
        color = LessTheme.colors.backgroundPrimary,
        shape = RoundedCornerShape(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(LessTheme.size.huge),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute?.contains(item.route::class.simpleName ?: "") == true
                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    item = item,
                    isSelected = isSelected,
                    onClick = {
                        val isCurrentRoute = currentRoute?.contains(item.route::class.simpleName ?: "") == true
                        if (!isCurrentRoute) {
                            // Tabs that should always refetch on entry — back stack entry
                            // (and therefore ViewModel) is rebuilt instead of restored.
                            val isLeavingNonCached = nonCachedTabs.any { name ->
                                currentRoute?.contains(name) == true
                            }
                            val isGoingToNonCached = item.route::class.simpleName in nonCachedTabs
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = !isLeavingNonCached
                                }
                                launchSingleTop = true
                                restoreState = !isGoingToNonCached
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
        targetValue = if (isSelected) LessTheme.colors.textIconsBrand else LessTheme.colors.textIconsGrey
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
        val label = stringResource(item.labelRes)
        Icon(
            painter = painterResource(resource = item.icon),
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(LessTheme.size.medium)
        )
        Text(
            text = label,
            style = LessTheme.typography.caption12Bold,
            color = iconColor,
            modifier = Modifier.padding(top = LessTheme.spacing.xxSmall)
        )
    }
}

private data class BottomNavItemData(
    val icon: DrawableResource,
    val labelRes: StringResource,
    val route: ClientRoute
)

private val nonCachedTabs: Set<String> = setOfNotNull(
    ClientRoute.Explore::class.simpleName,
    ClientRoute.Orders::class.simpleName,
    ClientRoute.Favorites::class.simpleName
)

private val bottomNavItems = listOf(
    BottomNavItemData(
        Res.drawable.ic_offers_24dp,
        Res.string.nav_offers,
        ClientRoute.Offers
    ),
    BottomNavItemData(
        Res.drawable.ic_explore_24dp,
        Res.string.nav_explore,
        ClientRoute.Explore
    ),
    BottomNavItemData(
        Res.drawable.ic_orders_24dp,
        Res.string.nav_orders,
        ClientRoute.Orders
    ),
    BottomNavItemData(
        Res.drawable.ic_saved_24dp,
        Res.string.nav_saved,
        ClientRoute.Favorites
    ),
    BottomNavItemData(
        Res.drawable.ic_more_24dp,
        Res.string.nav_more,
        ClientRoute.More
    )
)
