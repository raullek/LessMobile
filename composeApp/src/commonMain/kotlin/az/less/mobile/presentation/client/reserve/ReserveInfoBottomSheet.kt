package az.less.mobile.presentation.client.reserve

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.presentation.client.reserve.models.ReserveInfo

/**
 * Stateful Reserve Info Bottom Sheet
 * Shows reservation details when clicking on an order item
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2244-93472&m=dev
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReserveInfoBottomSheet(
    isVisible: Boolean,
    sheetState: SheetState,
    reserveInfo: ReserveInfo,
    onShowLocationClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = LessTheme.colors.backgroundSecond,
            contentColor = LessTheme.colors.textIconsBlack,
            shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
            dragHandle = null, // Remove default drag handle
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Custom drag handle inside the shaped container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(LessTheme.colors.borderPrimary)
                    )
                }
                
                // Content
                ReserveInfoBottomSheetContent(
                    reserveInfo = reserveInfo,
                    onShowLocationClicked = onShowLocationClicked
                )
            }
        }
    }
}

/**
 * Stateless Reserve Info Bottom Sheet Content
 * Pure UI that receives data and emits callbacks
 */
@Composable
private fun ReserveInfoBottomSheetContent(
    reserveInfo: ReserveInfo,
    onShowLocationClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = LessTheme.spacing.xxxLarge), // Home indicator space
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(LessTheme.spacing.small))

        // Header section
        HeaderSection(
            venueName = reserveInfo.venueName,
            pickupTime = reserveInfo.pickupTime
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        Divider()

        Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))

        // Reserve number section
        ReserveNumberSection(reserveNumber = reserveInfo.reserveNumber)

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        Divider()

        Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))

        // Details section
        DetailsSection(reserveInfo = reserveInfo)

        Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))

        // Action button
        ActionButton(onClick = onShowLocationClicked)

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
    }
}

/**
 * Header section with venue name and pickup time
 */
@Composable
private fun HeaderSection(
    venueName: String,
    pickupTime: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
    ) {
        Text(
            text = venueName,
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsBlack,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = pickupTime,
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsGrey,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Reserve number badge and label
 */
@Composable
private fun ReserveNumberSection(
    reserveNumber: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.large)
    ) {
        // Reserve number badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.textIconsBrand)
                .padding(horizontal = LessTheme.spacing.large, vertical = LessTheme.spacing.small)
        ) {
            Text(
                text = reserveNumber,
                style = LessTheme.typography.display36Semibold.copy(
                    fontSize = 28.sp
                ),
                color = LessTheme.colors.textIconsNested,
                textAlign = TextAlign.Center
            )
        }

        // Label
        Text(
            text = "Reserve number",
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Details section with date, prices, and subtotal
 */
@Composable
private fun DetailsSection(
    reserveInfo: ReserveInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
    ) {
        DetailRow(
            label = "Date",
            value = reserveInfo.date
        )

        DetailRow(
            label = "Price per piece",
            value = "${reserveInfo.pricePerPiece} ₼"
        )

        DetailRow(
            label = "Service fee",
            value = "${reserveInfo.serviceFee} ₼"
        )

        DetailRow(
            label = "Subtotal",
            value = "${reserveInfo.subtotal} ₼"
        )
    }
}

/**
 * Action button section
 */
@Composable
private fun ActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DsButton(
        text = "Show me location",
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        variant = ButtonVariant.Primary,
        size = ButtonSize.Large
    )
}

/**
 * Divider line
 */
@Composable
private fun Divider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(LessTheme.colors.borderPrimary)
    )
}

/**
 * Detail row component for displaying label-value pairs
 */
@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsGrey
        )
        Text(
            text = value,
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsBlack
        )
    }
}

