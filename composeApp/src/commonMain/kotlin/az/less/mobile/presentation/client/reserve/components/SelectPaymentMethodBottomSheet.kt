package az.less.mobile.presentation.client.reserve.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import az.less.mobile.presentation.client.reserve.models.CardType
import az.less.mobile.presentation.client.reserve.models.PaymentCard
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_payment_card_24dp
import org.jetbrains.compose.resources.vectorResource

/**
 * Bottom sheet for selecting payment method
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2244-35217&m=dev
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPaymentMethodBottomSheet(
    isVisible: Boolean,
    sheetState: SheetState,
    paymentCards: List<PaymentCard>,
    onCardSelected: (PaymentCard) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCardId by remember(paymentCards) {
        mutableStateOf(paymentCards.firstOrNull { it.isSelected }?.id)
    }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 34.dp) // Home indicator space
            ) {
                // Custom drag handle inside shaped container
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
                
                Spacer(modifier = Modifier.height(12.dp))

                // Credit and debit cards section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    // Section header
                    Text(
                        text = "Credit and debit cards",
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsGrey,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Payment cards list
                    paymentCards.forEach { card ->
                        PaymentCardItem(
                            card = card,
                            isSelected = selectedCardId == card.id,
                            onClick = {
                                selectedCardId = card.id
                            }
                        )
                    }
                }

                // Continue button
                DsButton(
                    text = "Continue",
                    onClick = {
                        val selectedCard = paymentCards.firstOrNull { it.id == selectedCardId }
                        if (selectedCard != null) {
                            onCardSelected(selectedCard)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    variant = ButtonVariant.Primary,
                    size = ButtonSize.Large,
                    enabled = selectedCardId != null
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Single payment card item row
 */
@Composable
private fun PaymentCardItem(
    card: PaymentCard,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Card icon placeholder
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(LessTheme.colors.backgroundPrimary)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_payment_card_24dp),
                contentDescription = null,
                tint = LessTheme.colors.textIconsGrey,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Card info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = when (card.type) {
                    CardType.MASTERCARD -> "Mastercard •••• ${card.lastFourDigits}"
                    CardType.VISA -> "Visa •••• ${card.lastFourDigits}"
                    CardType.ADD_NEW -> "Add new card"
                },
                style = LessTheme.typography.body16Regular,
                color = LessTheme.colors.textIconsBlack
            )

            // Show "Selected" indicator for the selected card
            if (isSelected && card.type != CardType.ADD_NEW) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Green checkmark icon (using a simple text icon for now)
                    Text(
                        text = "✓",
                        style = LessTheme.typography.body14Regular,
                        color = LessTheme.colors.textIconsBrand
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Selected",
                        style = LessTheme.typography.body14Regular,
                        color = LessTheme.colors.textIconsGrey
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Radio button
        DsRadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }

    // Bottom border for each item (except for "Add new card" which is the last item)
    if (card.type != CardType.ADD_NEW) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 68.dp) // Align with text
                .background(LessTheme.colors.borderPrimary)
        )
    }
}

