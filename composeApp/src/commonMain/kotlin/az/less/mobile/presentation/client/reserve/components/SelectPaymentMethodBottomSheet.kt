package az.less.mobile.presentation.client.reserve.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
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
import lessmobile.composeapp.generated.resources.payment_add_new_card
import lessmobile.composeapp.generated.resources.payment_continue
import lessmobile.composeapp.generated.resources.payment_credit_debit_cards
import lessmobile.composeapp.generated.resources.payment_selected
import lessmobile.composeapp.generated.resources.test_master_card_logo
import lessmobile.composeapp.generated.resources.test_visa_card_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

private const val ADD_NEW_CARD_ID = "add_new_card"

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
    isRegisterCardLoading: Boolean = false,
    onCardSelected: (PaymentCard) -> Unit,
    onAddNewCard: () -> Unit,
    onDismiss: () -> Unit
) {
    // Default to "add new card" when no cards exist, otherwise first selected card
    var selectedId by remember(paymentCards) {
        val defaultId = paymentCards.firstOrNull { it.isSelected }?.id
            ?: if (paymentCards.isEmpty()) ADD_NEW_CARD_ID else null
        mutableStateOf(defaultId)
    }

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 34.dp)
            ) {
                // Custom drag handle
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
                        text = stringResource(Res.string.payment_credit_debit_cards),
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsGrey,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Payment cards list
                    paymentCards.forEach { card ->
                        PaymentCardItem(
                            card = card,
                            isSelected = selectedId == card.id,
                            onClick = { selectedId = card.id }
                        )
                    }

                    // Add new card row
                    AddNewCardItem(
                        isSelected = selectedId == ADD_NEW_CARD_ID,
                        onClick = { selectedId = ADD_NEW_CARD_ID }
                    )
                }

                // Continue button
                DsButton(
                    text = stringResource(Res.string.payment_continue),
                    onClick = {
                        if (selectedId == ADD_NEW_CARD_ID) {
                            onAddNewCard()
                        } else {
                            val selectedCard = paymentCards.firstOrNull { it.id == selectedId }
                            if (selectedCard != null) {
                                onCardSelected(selectedCard)
                                onDismiss()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    variant = ButtonVariant.Primary,
                    size = ButtonSize.Large,
                    enabled = selectedId != null,
                    isLoading = isRegisterCardLoading
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
        // Card icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(LessTheme.colors.backgroundPrimary)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            when (card.type) {
                CardType.VISA -> Image(
                    painter = painterResource(Res.drawable.test_visa_card_logo),
                    contentDescription = "Visa",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )
                CardType.MASTERCARD -> Image(
                    painter = painterResource(Res.drawable.test_master_card_logo),
                    contentDescription = "Mastercard",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )
                CardType.ADD_NEW -> Icon(
                    imageVector = vectorResource(Res.drawable.ic_payment_card_24dp),
                    contentDescription = null,
                    tint = LessTheme.colors.textIconsGrey,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Card info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = card.displayName.ifEmpty {
                    when (card.type) {
                        CardType.MASTERCARD -> "Mastercard •••• ${card.lastFourDigits}"
                        CardType.VISA -> "Visa •••• ${card.lastFourDigits}"
                        CardType.ADD_NEW -> stringResource(Res.string.payment_add_new_card)
                    }
                },
                style = LessTheme.typography.body16Regular,
                color = LessTheme.colors.textIconsBlack
            )

            if (isSelected) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "\u2713",
                        style = LessTheme.typography.body14Regular,
                        color = LessTheme.colors.textIconsBrand
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = stringResource(Res.string.payment_selected),
                        style = LessTheme.typography.body14Regular,
                        color = LessTheme.colors.textIconsGrey
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        DsRadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }

    // Bottom border
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(start = 68.dp)
            .background(LessTheme.colors.borderPrimary)
    )
}

/**
 * Add new card item row with radio button
 */
@Composable
private fun AddNewCardItem(
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
        // Card add icon
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

        Text(
            text = stringResource(Res.string.payment_add_new_card),
            style = LessTheme.typography.body16Regular,
            color = LessTheme.colors.textIconsBlack,
            modifier = Modifier.weight(1f)
        )

        DsRadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }
}
