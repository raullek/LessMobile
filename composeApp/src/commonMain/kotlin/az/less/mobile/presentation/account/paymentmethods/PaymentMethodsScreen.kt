package az.less.mobile.presentation.account.paymentmethods

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.DsIcons
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsConfirmationBottomSheet
import az.less.designsystem.components.DsSectionHeader
import az.less.designsystem.components.DsToolBar
import az.less.mobile.navigation.MoreScreens
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import lessmobile.composeapp.generated.resources.ic_payment_card_24dp
import lessmobile.composeapp.generated.resources.test_apple_logo
import lessmobile.composeapp.generated.resources.test_google_logo
import lessmobile.composeapp.generated.resources.test_master_card_logo
import lessmobile.composeapp.generated.resources.test_visa_card_logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PaymentMethodsScreen(
    viewModel: PaymentMethodsViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PaymentMethodsSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is PaymentMethodsSideEffect.NavigateToAddCard -> {
                navController.navigate(MoreScreens.AddNewCard.route)
            }
            is PaymentMethodsSideEffect.NavigateToApplePay -> {
                // TODO: Navigate to Apple Pay setup
            }
            is PaymentMethodsSideEffect.NavigateToGooglePay -> {
                // TODO: Navigate to Google Pay setup
            }
            is PaymentMethodsSideEffect.ShowError -> {
                // TODO: Show error message
            }
        }
    }

    PaymentMethodsScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
    
    // Show delete confirmation bottom sheet
    state.cardToDelete?.let { cardToDelete ->
        val cardImage = when (cardToDelete.type) {
            PaymentMethodType.MASTERCARD -> painterResource(Res.drawable.test_master_card_logo)
            PaymentMethodType.VISA -> painterResource(Res.drawable.test_visa_card_logo)
            PaymentMethodType.APPLE_PAY -> painterResource(Res.drawable.test_apple_logo)
            PaymentMethodType.GOOGLE_PAY -> painterResource(Res.drawable.test_google_logo)
            PaymentMethodType.ADD_NEW_CARD -> painterResource(Res.drawable.ic_payment_card_24dp)
        }
        
        val cardTitle = when (cardToDelete.type) {
            PaymentMethodType.MASTERCARD -> "Delete Mastercard${cardToDelete.lastFourDigits?.let { " •••• $it" } ?: ""}"
            PaymentMethodType.VISA -> "Delete Visa${cardToDelete.lastFourDigits?.let { " •••• $it" } ?: ""}"
            PaymentMethodType.APPLE_PAY -> "Delete Apple Pay"
            PaymentMethodType.GOOGLE_PAY -> "Delete Google Pay"
            PaymentMethodType.ADD_NEW_CARD -> "Delete Card"
        }
        
        DsConfirmationBottomSheet(
            image = cardImage,
            title = cardTitle,
            description = "Are you sure to delete this card from app, after deleting you should select other card for payment",
            primaryButtonText = "Delete",
            secondaryButtonText = "Cancel",
            onPrimaryClick = { viewModel.onIntent(PaymentMethodsIntent.OnConfirmDelete) },
            onSecondaryClick = { viewModel.onIntent(PaymentMethodsIntent.OnCancelDelete) },
            onDismiss = { viewModel.onIntent(PaymentMethodsIntent.OnCancelDelete) }
        )
    }
}

@Composable
fun PaymentMethodsScreenContent(
    state: PaymentMethodsState,
    onIntent: (PaymentMethodsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LessTheme.colors.backgroundPrimary)
    ) {
        DsToolBar(
            title = "Payment methods",
            onBackClick = { onIntent(PaymentMethodsIntent.OnBackClicked) }
        )

        LazyColumn {
            // Credit & Debit Cards Section
            item {
                Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
            }
            
            item {
                DsSectionHeader(
                    title = "Credit & Debit Cards",
                    modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                )
            }

            items(
                items = state.creditDebitCards,
                key = { it.id }
            ) { paymentMethod ->
                PaymentMethodItem(
                    paymentMethod = paymentMethod,
                    onItemClick = {
                        if (paymentMethod.type == PaymentMethodType.ADD_NEW_CARD) {
                            onIntent(PaymentMethodsIntent.OnAddNewCardClicked)
                        } else {
                            onIntent(PaymentMethodsIntent.OnCardSelected(paymentMethod.id))
                        }
                    },
                    onDeleteClick = {
                        onIntent(PaymentMethodsIntent.OnDeleteCardClicked(paymentMethod.id))
                    },
                    modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                )
            }

            // Add new card item
            item {
                PaymentMethodItem(
                    paymentMethod = PaymentMethod(
                        id = "add_card",
                        type = PaymentMethodType.ADD_NEW_CARD
                    ),
                    onItemClick = {
                        onIntent(PaymentMethodsIntent.OnAddNewCardClicked)
                    },
                    onDeleteClick = null,
                    modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                )
            }

            // Other Methods Section
            item {
                Spacer(modifier = Modifier.height(LessTheme.spacing.large))
            }

            item {
                DsSectionHeader(
                    title = "Other Methods",
                    modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                )
            }

            items(
                items = state.otherMethods,
                key = { it.id }
            ) { paymentMethod ->
                PaymentMethodItem(
                    paymentMethod = paymentMethod,
                    onItemClick = {
                        when (paymentMethod.type) {
                            PaymentMethodType.APPLE_PAY -> {
                                onIntent(PaymentMethodsIntent.OnApplePayClicked)
                            }
                            PaymentMethodType.GOOGLE_PAY -> {
                                onIntent(PaymentMethodsIntent.OnGooglePayClicked)
                            }
                            else -> {}
                        }
                    },
                    onDeleteClick = null,
                    modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                )
            }

            item {
                Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
            }
        }
    }
}

@Composable
private fun PaymentMethodItem(
    paymentMethod: PaymentMethod,
    onItemClick: () -> Unit,
    onDeleteClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val displayName = when (paymentMethod.type) {
        PaymentMethodType.MASTERCARD -> "Mastercard${paymentMethod.lastFourDigits?.let { " .... $it" } ?: ""}"
        PaymentMethodType.VISA -> "Visa${paymentMethod.lastFourDigits?.let { " .... $it" } ?: ""}"
        PaymentMethodType.APPLE_PAY -> "Apple Pay"
        PaymentMethodType.GOOGLE_PAY -> "Google Pay"
        PaymentMethodType.ADD_NEW_CARD -> "Add new card"
    }

    val showDeleteIcon = onDeleteClick != null && paymentMethod.type != PaymentMethodType.ADD_NEW_CARD
    val showChevron = paymentMethod.type == PaymentMethodType.ADD_NEW_CARD || 
                      paymentMethod.type == PaymentMethodType.APPLE_PAY || 
                      paymentMethod.type == PaymentMethodType.GOOGLE_PAY
    val showSelected = paymentMethod.isSelected && paymentMethod.type != PaymentMethodType.ADD_NEW_CARD

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LessTheme.colors.backgroundPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onItemClick)
                .padding(vertical = LessTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
        ) {
            // Leading icon/logo with gray background
            Box(
                modifier = Modifier
                    .size(LessTheme.size.medium)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.elementsSecondaryElement)
                    .padding(LessTheme.spacing.xxxSmall),
                contentAlignment = Alignment.Center
            ) {
                when (paymentMethod.type) {
                    PaymentMethodType.MASTERCARD -> {
                        Image(
                            painter = painterResource(Res.drawable.test_master_card_logo),
                            contentDescription = "Mastercard",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    PaymentMethodType.VISA -> {
                        Image(
                            painter = painterResource(Res.drawable.test_visa_card_logo),
                            contentDescription = "Visa",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    PaymentMethodType.APPLE_PAY -> {
                        Image(
                            painter = painterResource(Res.drawable.test_apple_logo),
                            contentDescription = "Apple Pay",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    PaymentMethodType.GOOGLE_PAY -> {
                        Image(
                            painter = painterResource(Res.drawable.test_google_logo),
                            contentDescription = "Google Pay",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    PaymentMethodType.ADD_NEW_CARD -> {
                        Icon(
                            painter = painterResource(Res.drawable.ic_payment_card_24dp),
                            contentDescription = "Add new card",
                            tint = LessTheme.colors.textIconsBlack,
                            modifier = Modifier.size(LessTheme.size.medium)
                        )
                    }
                }
            }

            // Title and subtitle
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall)
            ) {
                Text(
                    text = displayName,
                    style = LessTheme.typography.body16Medium,
                    color = LessTheme.colors.textIconsBlack
                )
                
                if (showSelected) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall)
                    ) {
                        Icon(
                            painter = DsIcons.CheckSuccess,
                            contentDescription = null,
                            tint = LessTheme.colors.elementsPrimaryBrand,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Selected",
                            style = LessTheme.typography.body14Regular,
                            color = LessTheme.colors.elementsPrimaryBrand
                        )
                    }
                }
            }

            // Trailing content
            Row(
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showDeleteIcon) {
                    val deleteInteractionSource = remember { MutableInteractionSource() }
                    Box(
                        modifier = Modifier
                            .clickable(
                                onClick = { onDeleteClick?.invoke() },
                                indication = null,
                                interactionSource = deleteInteractionSource
                            )
                            .padding(LessTheme.spacing.xxxSmall)
                    ) {
                        Icon(
                            painter = DsIcons.Delete,
                            contentDescription = "Delete",
                            tint = LessTheme.colors.textIconsThird,
                            modifier = Modifier.size(LessTheme.size.medium)
                        )
                    }
                }
                
                if (showChevron) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_chevron_right_24dp),
                        contentDescription = null,
                        tint = LessTheme.colors.textIconsThird,
                        modifier = Modifier.size(LessTheme.size.medium)
                    )
                }
            }
        }

        // Bottom divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = LessTheme.spacing.xxLarge)
                .background(LessTheme.colors.borderPrimary)
        )
    }
}

