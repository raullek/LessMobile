package az.less.mobile.presentation.reserve

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_down24dp
import lessmobile.composeapp.generated.resources.ic_chevron_left_24dp
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import lessmobile.composeapp.generated.resources.ic_minus_24dp
import lessmobile.composeapp.generated.resources.ic_plus_24dp
import lessmobile.composeapp.generated.resources.test_merchant_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful ReserveScreen that connects to ViewModel
 * This is the entry point used by other screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReserveScreen(
    isVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    viewModel: ReserveViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    
    // Collect side effects
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ReserveSideEffect.NavigateBack -> {
                onDismiss()
            }
            is ReserveSideEffect.NavigateToPaymentMethods -> {
                // Handle navigation to payment methods
            }
            is ReserveSideEffect.NavigateToAddressSelection -> {
                // Handle navigation to address selection
            }
            is ReserveSideEffect.ShowError -> {
                // Show error snackbar
            }
            is ReserveSideEffect.OrderPlaced -> {
                // Handle order placed
                onDismiss()
            }
        }
    }
    
    // Only render ModalBottomSheet when visible
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
                // Prevent dismiss on outside click or drag
                // Only allow dismiss via back button
            },
            sheetState = sheetState,
            containerColor = Color.Transparent,
            contentColor = LessTheme.colors.textIconsBlack,
            sheetGesturesEnabled = false,
            shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
            dragHandle = null,
            scrimColor = Color.Black.copy(alpha = 0.5f),
            contentWindowInsets = {WindowInsets(0, 0, 0, 0)}
        ) {
            // Cancel button in transparent area (similar to Stack Overflow example)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = PaddingValues(bottom = LessTheme.spacing.xxLarge, start = LessTheme.spacing.medium)),
                contentAlignment = Alignment.TopStart
            ) {
                FilledIconButton(
                    modifier = Modifier.size(LessTheme.size.xxLarge),
                    onClick = {
                        viewModel.onIntent(ReserveIntent.OnBackClicked)
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = LessTheme.colors.backgroundPrimary
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(LessTheme.size.medium).fillMaxSize(),
                        imageVector = vectorResource(Res.drawable.ic_chevron_left_24dp),
                        tint = LessTheme.colors.textIconsBrand,
                        contentDescription ="")
                }
            }
            
            ReserveScreenContent(
                state = state,
                onIntent = viewModel::onIntent,
                onBackClicked = {
                    viewModel.onIntent(ReserveIntent.OnBackClicked)
                }
            )
        }
    }
}

/**
 * Stateless ReserveScreen UI implementation
 * Pure UI that receives state and emits intents
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2244-34907&m=dev
 */
@Composable
private fun ReserveScreenContent(
    state: ReserveState,
    onIntent: (ReserveIntent) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
            .background(color = LessTheme.colors.backgroundSecond)
            .fillMaxWidth()
            .padding(LessTheme.spacing.medium) // Inner padding
    ) {
        
        // Header with venue info
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = state.venueName,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack
            )
            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))
            Text(
                text = state.pickupTime,
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsGrey
            )
        }
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
        
        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LessTheme.colors.elementsSecondaryElement)
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        
        // Quantity selector
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Decrement button
                FilledIconButton(
                    onClick = { onIntent(ReserveIntent.OnDecrementQuantity) },
                    shape = RoundedCornerShape(LessTheme.radius.medium),
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = LessTheme.colors.elementsPrimaryElement
                    )
                ) {
                   Icon(
                       vectorResource(Res.drawable.ic_minus_24dp),
                       tint = LessTheme.colors.textIconsBrand,
                       contentDescription = "back_icon")
                }
                
                Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
                
                // Quantity display
                Text(
                    text = state.quantity.toString(),
                    style = LessTheme.typography.display36Semibold,
                    color = LessTheme.colors.textIconsBlack,
                    modifier = Modifier.width(30.dp),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
                
                // Increment button
                FilledIconButton(
                    onClick = { onIntent(ReserveIntent.OnIncrementQuantity) },
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(LessTheme.radius.medium),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = LessTheme.colors.elementsPrimaryElement
                    )
                ) {
                  Icon(painter = painterResource(Res.drawable.ic_plus_24dp),
                      tint = LessTheme.colors.textIconsBrand,
                      contentDescription = "plus")
                }
            }
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.small))
            
            // Items left text
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${state.itemsLeft} items left",
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsGrey
                )
            }
        }
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        
        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LessTheme.colors.elementsSecondaryElement)
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
        
        // See more deals section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onIntent(ReserveIntent.OnSeeMoreDealsClicked) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Venue image placeholder
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.elementsSecondaryElement)
            ){
                Image(
                    painter = painterResource(Res.drawable.test_merchant_logo),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "")
            }
            
            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Belgian Coffee",
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
                Spacer(modifier = Modifier.height(LessTheme.spacing.xxxSmall))
                Text(
                    text = "See more deals from this venue",
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey
                )
            }
            
            Icon(
                imageVector = vectorResource(Res.drawable.ic_chevron_right_24dp),
                contentDescription = "See more",
                tint = LessTheme.colors.textIconsBrand,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        
        // Price breakdown
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Price per piece
            PriceRow(
                label = "Price per piece",
                value = "${state.pricePerPiece} ₼"
            )
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
            
            // Service fee
            PriceRow(
                label = "Service fee",
                value = "${state.serviceFee} ₼"
            )
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
            
            // Subtotal
            PriceRow(
                label = "Subtotal",
                value = "${state.subtotal} ₼",
            )
        }
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        
        // Payment method selector
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsPrimaryElement)
                .clickable { onIntent(ReserveIntent.OnPaymentMethodClicked) }
                .padding(horizontal = LessTheme.spacing.medium),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Payment icon placeholder
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(LessTheme.radius.small))
                            .background(LessTheme.colors.elementsSecondaryElement)
                    )
                    
                    Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
                    
                    Text(
                        text = state.paymentMethodDisplay.ifEmpty { "Select payment method" },
                        style = LessTheme.typography.body16Regular,
                        color = LessTheme.colors.textIconsBlack
                    )
                }
                
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_chevron_down24dp),
                    contentDescription = "Select payment",
                    tint = LessTheme.colors.textIconsBrand,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        
        // Place order button
        DsButton(
            text = "Place order",
            onClick = { onIntent(ReserveIntent.OnPlaceOrderClicked) },
            modifier = Modifier
                .fillMaxWidth(),
            variant = ButtonVariant.Primary,
            size = ButtonSize.Large
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
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

