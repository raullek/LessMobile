package az.less.mobile.presentation.client.reserve

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.presentation.client.reserve.models.OrderAccepted
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.order_accepted_title
import lessmobile.composeapp.generated.resources.order_accepted_description
import lessmobile.composeapp.generated.resources.order_accepted_go_orders
import lessmobile.composeapp.generated.resources.action_home
import org.jetbrains.compose.resources.stringResource

/**
 * Stateful Order Accepted Screen
 * This is the entry point used by navigation
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2244-35280&m=dev
 */
@Composable
fun OrderAcceptedScreen(
    navController: NavController,
    orderInfo: OrderAccepted,

) {
    OrderAcceptedScreenContent(
        orderInfo = orderInfo,
        onGoToOrdersClicked = {
            navController.navigate("orders",navOptions {
            popUpTo("offers"){inclusive =true}
            launchSingleTop = true
        })},

        onGoToHomeClicked = { navController.navigate("offers",navOptions {
            popUpTo("offers"){inclusive =true}
            launchSingleTop = true })  }
    )
}

/**
 * Stateless Order Accepted Screen Content
 * Pure UI that receives data and emits callbacks
 */
@Composable
private fun OrderAcceptedScreenContent(
    orderInfo: OrderAccepted,
    onGoToOrdersClicked: () -> Unit,
    onGoToHomeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top spacer to push content to center
        Spacer(modifier = Modifier.weight(1f))

        // Center content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Success checkmark icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(LessTheme.colors.textIconsBrand),
                contentAlignment = Alignment.Center
            ) {
                // Checkmark using a simple text icon (temporary placeholder)
                Text(
                    text = "✓",
                    style = LessTheme.typography.display36Semibold,
                    color = Color.White,
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text(
                text = stringResource(Res.string.order_accepted_title),
                style = LessTheme.typography.body16Semibold.copy(
                    fontSize = 20.sp
                ),
                color = LessTheme.colors.textIconsBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = stringResource(Res.string.order_accepted_description),
                style = LessTheme.typography.body14Regular.copy(
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                ),
                color = LessTheme.colors.textIconsGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Order number badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(LessTheme.colors.textIconsBrand)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = orderInfo.orderNumber,
                    style = LessTheme.typography.display36Semibold.copy(
                        fontSize = 28.sp
                    ),
                    color = LessTheme.colors.textIconsNested,
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Pickup time
            Text(
                text = orderInfo.pickupTime,
                style = LessTheme.typography.body14Medium,
                color = LessTheme.colors.textIconsGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Bottom spacer
        Spacer(modifier = Modifier.weight(1f))

        // Bottom buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            // Go Orders button
            DsButton(
                text = stringResource(Res.string.order_accepted_go_orders),
                onClick = onGoToOrdersClicked,
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Home button (secondary style)
            DsButton(
                text = stringResource(Res.string.action_home),
                onClick = onGoToHomeClicked,
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Secondary,
                size = ButtonSize.Large
            )

            Spacer(modifier = Modifier.height(34.dp)) // Home indicator space
        }
    }
}

