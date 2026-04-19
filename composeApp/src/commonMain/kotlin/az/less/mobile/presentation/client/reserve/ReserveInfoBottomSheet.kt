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
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.presentation.client.reserve.models.ReserveInfo
import az.less.mobile.utils.formatPrice
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.reserve_info_reserve_number
import lessmobile.composeapp.generated.resources.reserve_info_date
import lessmobile.composeapp.generated.resources.reserve_price_per_piece
import lessmobile.composeapp.generated.resources.reserve_service_fee
import lessmobile.composeapp.generated.resources.reserve_subtotal
import lessmobile.composeapp.generated.resources.reserve_info_show_location
import org.jetbrains.compose.resources.stringResource

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
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header section - pb=20 from design
        HeaderSection(
            venueName = reserveInfo.venueName,
            pickupTime = reserveInfo.pickupTime
        )

        // Divider + 28dp gap
        Divider()

        Spacer(modifier = Modifier.height(28.dp))

        // Reserve number section
        ReserveNumberSection(reserveNumber = reserveInfo.reserveNumber)

        Spacer(modifier = Modifier.height(28.dp))

        // Divider + 28dp gap
        Divider()

        Spacer(modifier = Modifier.height(28.dp))

        // Details section + 24dp gap to button
        DetailsSection(reserveInfo = reserveInfo)

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Action button - px=16, pb=16
        ActionButton(onClick = onShowLocationClicked)

        // Home indicator space - 34dp
        Spacer(modifier = Modifier.height(34.dp))
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
            .padding(horizontal = LessTheme.spacing.medium)
            .padding(top = LessTheme.spacing.xSmall, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
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
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Reserve number badge - h=56, px=20, py=12, rounded=12
        Box(
            modifier = Modifier
                .height(56.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.textIconsBrand)
                .padding(horizontal = 20.dp, vertical = LessTheme.spacing.small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = reserveNumber,
                style = LessTheme.typography.title28Medium,
                color = LessTheme.colors.textIconsNested,
                textAlign = TextAlign.Center
            )
        }

        // Label
        Text(
            text = stringResource(Res.string.reserve_info_reserve_number),
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
            label = stringResource(Res.string.reserve_info_date),
            value = reserveInfo.date
        )

        DetailRow(
            label = stringResource(Res.string.reserve_price_per_piece),
            value = "${reserveInfo.pricePerPiece.formatPrice()} ₼"
        )

        DetailRow(
            label = stringResource(Res.string.reserve_service_fee),
            value = "${reserveInfo.serviceFee.formatPrice()} ₼"
        )

        DetailRow(
            label = stringResource(Res.string.reserve_subtotal),
            value = "${reserveInfo.subtotal.formatPrice()} ₼"
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
        text = stringResource(Res.string.reserve_info_show_location),
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
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsBlack
        )
    }
}

