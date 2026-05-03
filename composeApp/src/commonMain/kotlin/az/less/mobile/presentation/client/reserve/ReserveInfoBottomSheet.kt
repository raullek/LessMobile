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
import lessmobile.composeapp.generated.resources.reserve_info_date
import lessmobile.composeapp.generated.resources.reserve_info_reserve_number
import lessmobile.composeapp.generated.resources.reserve_info_show_location
import lessmobile.composeapp.generated.resources.reserve_price_per_piece
import lessmobile.composeapp.generated.resources.reserve_service_fee
import lessmobile.composeapp.generated.resources.reserve_subtotal
import org.jetbrains.compose.resources.stringResource

/**
 * Bottom sheet for an *active* (not yet picked up) order. Single CTA:
 * "Show me location" — deep-links into the venue map.
 *
 * For completed orders, see [PreviousOrderInfoBottomSheet] — that sheet owns
 * the review / support actions, since they belong to a different lifecycle.
 *
 * Figma: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/?node-id=2244-93472
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
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ReserveInfoDragHandle()

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ReserveInfoHeader(
                        title = reserveInfo.venueName,
                        subtitle = reserveInfo.pickupTime,
                        subtitleIsBrand = false
                    )

                    ReserveInfoDivider()
                    Spacer(modifier = Modifier.height(28.dp))

                    ReserveNumberBlock(
                        reserveNumber = reserveInfo.reserveNumber,
                        statusBadgeText = null
                    )

                    Spacer(modifier = Modifier.height(28.dp))
                    ReserveInfoDivider()
                    Spacer(modifier = Modifier.height(28.dp))

                    ReserveDetailsSection(
                        date = reserveInfo.date,
                        pricePerPiece = reserveInfo.pricePerPiece,
                        serviceFee = reserveInfo.serviceFee,
                        subtotal = reserveInfo.subtotal
                    )

                    Spacer(modifier = Modifier.height(LessTheme.spacing.large))

                    DsButton(
                        text = stringResource(Res.string.reserve_info_show_location),
                        onClick = onShowLocationClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LessTheme.spacing.medium),
                        variant = ButtonVariant.Primary,
                        size = ButtonSize.Large
                    )

                    Spacer(modifier = Modifier.height(34.dp))
                }
            }
        }
    }
}

// region Shared building blocks (used by both this sheet and
// [PreviousOrderInfoBottomSheet] — kept `internal` so the two sheets stay in
// the same package boundary without exposing them to the rest of the app).

@Composable
internal fun ReserveInfoDragHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
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
}

@Composable
internal fun ReserveInfoHeader(
    title: String,
    subtitle: String,
    subtitleIsBrand: Boolean,
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
            text = title,
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsBlack,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = subtitle,
            style = LessTheme.typography.body16Medium,
            color = if (subtitleIsBrand) {
                LessTheme.colors.textIconsBrand
            } else {
                LessTheme.colors.textIconsGrey
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
internal fun ReserveNumberBlock(
    reserveNumber: String,
    statusBadgeText: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
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
                style = LessTheme.typography.title28Bold,
                color = LessTheme.colors.textIconsNested,
                textAlign = TextAlign.Center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.reserve_info_reserve_number),
                style = LessTheme.typography.body16Medium,
                color = LessTheme.colors.textIconsBlack,
                textAlign = TextAlign.Center
            )
            if (statusBadgeText != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(1000.dp))
                        .background(LessTheme.colors.textIconsBrand)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusBadgeText,
                        style = LessTheme.typography.caption12Semibold,
                        color = LessTheme.colors.textIconsNested
                    )
                }
            }
        }
    }
}

@Composable
internal fun ReserveDetailsSection(
    date: String,
    pricePerPiece: Double,
    serviceFee: Double,
    subtotal: Double,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
    ) {
        ReserveInfoDetailRow(
            label = stringResource(Res.string.reserve_info_date),
            value = date
        )
        ReserveInfoDetailRow(
            label = stringResource(Res.string.reserve_price_per_piece),
            value = "${pricePerPiece.formatPrice()} ₼"
        )
        ReserveInfoDetailRow(
            label = stringResource(Res.string.reserve_service_fee),
            value = "${serviceFee.formatPrice()} ₼"
        )
        ReserveInfoDetailRow(
            label = stringResource(Res.string.reserve_subtotal),
            value = "${subtotal.formatPrice()} ₼"
        )
    }
}

@Composable
internal fun ReserveInfoDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(LessTheme.colors.borderPrimary)
    )
}

@Composable
private fun ReserveInfoDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Figma marks these rows as Medium 500, but Baloo 2 Medium renders
        // visibly lighter in Compose than in Figma's preview, so the design
        // *looks* like Semibold/Bold weight. Bump both label and value up to
        // Semibold so the on-device rendering matches what's seen in Figma.
        Text(
            text = label,
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsGrey
        )
        Text(
            text = value,
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsBlack
        )
    }
}

// endregion
