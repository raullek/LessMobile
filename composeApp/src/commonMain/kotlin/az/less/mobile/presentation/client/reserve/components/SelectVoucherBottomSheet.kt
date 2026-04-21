package az.less.mobile.presentation.client.reserve.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsRadioButton
import az.less.mobile.presentation.client.reserve.models.Voucher
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_gift_24dp
import lessmobile.composeapp.generated.resources.select_voucher_title
import lessmobile.composeapp.generated.resources.payment_continue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Bottom sheet for selecting a voucher
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2561-77803&m=dev
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectVoucherBottomSheet(
    isVisible: Boolean,
    sheetState: SheetState,
    vouchers: List<Voucher>,
    onVoucherSelected: (Voucher) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedVoucherId by remember(vouchers) {
        mutableStateOf(vouchers.firstOrNull { it.isSelected }?.id)
    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = LessTheme.colors.backgroundSecond,
            contentColor = LessTheme.colors.textIconsBlack,
            shape = RoundedCornerShape(topStart = LessTheme.radius.medium, topEnd = LessTheme.radius.medium),
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LessTheme.size.xLarge)
            ) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = LessTheme.spacing.xSmall),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(LessTheme.size.large)
                            .height(3.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(LessTheme.colors.borderPrimary)
                    )
                }

                // Title
                Text(
                    text = stringResource(Res.string.select_voucher_title),
                    style = LessTheme.typography.body16Medium,
                    color = LessTheme.colors.textIconsBlack,
                    modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                )

                Spacer(modifier = Modifier.height(LessTheme.spacing.small))

                // Vouchers list (scrollable)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = LessTheme.spacing.large)
                ) {
                    vouchers.forEachIndexed { index, voucher ->
                        VoucherItem(
                            voucher = voucher,
                            isSelected = selectedVoucherId == voucher.id,
                            showDivider = index < vouchers.size - 1,
                            onClick = {
                                selectedVoucherId = voucher.id
                            }
                        )
                    }
                }

                // Continue button
                DsButton(
                    text = stringResource(Res.string.payment_continue),
                    onClick = {
                        val selectedVoucher = vouchers.firstOrNull { it.id == selectedVoucherId }
                        if (selectedVoucher != null) {
                            onVoucherSelected(selectedVoucher)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = LessTheme.spacing.medium),
                    variant = ButtonVariant.Primary,
                    size = ButtonSize.Large,
                    enabled = selectedVoucherId != null
                )

                Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
            }
        }
    }
}

/**
 * Single voucher item row
 */
@Composable
private fun VoucherItem(
    voucher: Voucher,
    isSelected: Boolean,
    showDivider: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = LessTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gift icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.backgroundPrimary)
                .padding(LessTheme.spacing.xSmall),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_gift_24dp),
                contentDescription = null,
                tint = LessTheme.colors.textIconsBrand,
                modifier = Modifier.size(LessTheme.size.medium)
            )
        }

        Spacer(modifier = Modifier.width(LessTheme.spacing.small))

        // Voucher info with divider
        Row(
            modifier = Modifier
                .weight(1f)
                .height(65.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = voucher.name,
                    style = LessTheme.typography.body16Regular,
                    color = LessTheme.colors.textIconsBlack
                )
                Spacer(modifier = Modifier.height(LessTheme.spacing.xxxSmall))
                Text(
                    text = "Expires in ${voucher.expiresInDays} days",
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsGrey
                )
            }

            Spacer(modifier = Modifier.width(LessTheme.spacing.small))

            // Radio button
            DsRadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }

    // Divider (aligned with text content)
    if (showDivider) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 68.dp)
                .height(1.dp)
                .background(LessTheme.colors.borderPrimary)
        )
    }
}
