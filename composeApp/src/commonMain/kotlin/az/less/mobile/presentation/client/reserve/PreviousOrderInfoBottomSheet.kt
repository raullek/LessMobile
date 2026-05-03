package az.less.mobile.presentation.client.reserve

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.presentation.client.reserve.models.PreviousOrderInfo
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.reserve_info_contact_support
import lessmobile.composeapp.generated.resources.reserve_info_leave_review
import lessmobile.composeapp.generated.resources.reserve_info_status_picked_up
import org.jetbrains.compose.resources.stringResource

/**
 * Bottom sheet shown when the user taps a *previous* (picked-up / completed)
 * order. Owns its own actions — "Leave review" and "Contact with support" —
 * which are unrelated to the active-order map deep-link, so this is kept as a
 * separate composable from [ReserveInfoBottomSheet].
 *
 * Visually it shares the header / reserve-number / detail-rows building blocks
 * with [ReserveInfoBottomSheet] (declared `internal` in that file) so the two
 * sheets stay pixel-identical for the parts that are the same.
 *
 * Figma: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/?node-id=3057-8993
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviousOrderInfoBottomSheet(
    isVisible: Boolean,
    sheetState: SheetState,
    info: PreviousOrderInfo,
    onLeaveReviewClicked: () -> Unit,
    onContactSupportClicked: () -> Unit,
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
                    // Header — pickup-status line in brand-green per Figma.
                    ReserveInfoHeader(
                        title = info.boxTitle,
                        subtitle = info.pickedUpOn,
                        subtitleIsBrand = true
                    )

                    ReserveInfoDivider()
                    Spacer(modifier = Modifier.height(28.dp))

                    // Reserve number + the small "Picked up" status pill.
                    ReserveNumberBlock(
                        reserveNumber = info.reserveNumber,
                        statusBadgeText = stringResource(Res.string.reserve_info_status_picked_up)
                    )

                    Spacer(modifier = Modifier.height(28.dp))
                    ReserveInfoDivider()
                    Spacer(modifier = Modifier.height(28.dp))

                    ReserveDetailsSection(
                        date = info.date,
                        pricePerPiece = info.pricePerPiece,
                        serviceFee = info.serviceFee,
                        subtotal = info.subtotal
                    )

                    Spacer(modifier = Modifier.height(LessTheme.spacing.large))

                    // CTA pair — "Leave review" (Primary) + "Contact with
                    // support" (Secondary) stacked with 8dp spacing per Figma.
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LessTheme.spacing.medium),
                        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
                    ) {
                        DsButton(
                            text = stringResource(Res.string.reserve_info_leave_review),
                            onClick = onLeaveReviewClicked,
                            modifier = Modifier.fillMaxWidth(),
                            variant = ButtonVariant.Primary,
                            size = ButtonSize.Large
                        )
                        DsButton(
                            text = stringResource(Res.string.reserve_info_contact_support),
                            onClick = onContactSupportClicked,
                            modifier = Modifier.fillMaxWidth(),
                            variant = ButtonVariant.Secondary,
                            size = ButtonSize.Large
                        )
                    }

                    Spacer(modifier = Modifier.height(34.dp))
                }
            }
        }
    }
}
