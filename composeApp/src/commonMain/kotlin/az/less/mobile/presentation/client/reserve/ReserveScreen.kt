package az.less.mobile.presentation.client.reserve

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.utils.formatPrice
import az.less.mobile.presentation.client.reserve.components.LotSizeInfoBottomSheet
import az.less.mobile.presentation.client.reserve.components.PaymentMethodShimmer
import az.less.mobile.presentation.client.reserve.components.ReserveScreenShimmer
import az.less.mobile.presentation.client.reserve.components.SelectPaymentMethodBottomSheet
import az.less.mobile.presentation.client.reserve.components.SelectVoucherBottomSheet
import az.less.mobile.presentation.client.reserve.models.CardType
import az.less.mobile.presentation.client.reserve.models.OrderAccepted
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_down24dp
import lessmobile.composeapp.generated.resources.ic_close_24dp
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import lessmobile.composeapp.generated.resources.ic_gift_24dp
import lessmobile.composeapp.generated.resources.ic_info_24dp
import lessmobile.composeapp.generated.resources.ic_minus_24dp
import lessmobile.composeapp.generated.resources.ic_payment_card_24dp
import lessmobile.composeapp.generated.resources.ic_plus_24dp
import lessmobile.composeapp.generated.resources.ill_venue_placeholder
import lessmobile.composeapp.generated.resources.reserve_and_pay
import lessmobile.composeapp.generated.resources.reserve_price_per_piece
import lessmobile.composeapp.generated.resources.reserve_select_voucher
import lessmobile.composeapp.generated.resources.reserve_service_fee
import lessmobile.composeapp.generated.resources.reserve_subtotal
import lessmobile.composeapp.generated.resources.voucher_discount
import lessmobile.composeapp.generated.resources.test_master_card_logo
import lessmobile.composeapp.generated.resources.test_visa_card_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
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
    offerId: String,
    onDismiss: () -> Unit,
    onOrderPlaced: (OrderAccepted) -> Unit = {},
    onNavigateToMerchant: (String) -> Unit = {},
    onNavigateToAddCardWebView: (String) -> Unit = {},
    viewModel: ReserveViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()

    // Initialize with offerId
    LaunchedEffect(offerId) {
        if (offerId.isNotEmpty()) {
            viewModel.onIntent(ReserveIntent.Initialize(offerId))
        }
    }

    // Lot size info bottom sheet state
    val lotSizeInfoSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Voucher selection bottom sheet state
    val voucherSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Collect side effects
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ReserveSideEffect.NavigateBack -> {
                onDismiss()
            }
            is ReserveSideEffect.OpenRedirectUrl -> {
                onNavigateToAddCardWebView(sideEffect.url)
            }
            is ReserveSideEffect.NavigateToAddressSelection -> {
                // Handle navigation to address selection
            }
            is ReserveSideEffect.ShowError -> {
                // Show error snackbar
            }
            is ReserveSideEffect.OrderPlaced -> {
                // Handle order placed - navigate to order accepted screen
                onOrderPlaced(sideEffect.orderInfo)
                onDismiss()
            }
            is ReserveSideEffect.NavigateToMerchant -> {
                // Navigate to merchant screen
                onNavigateToMerchant(sideEffect.merchantId)
                onDismiss()
            }
        }
    }

    // Only render ModalBottomSheet when visible
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
            BoxWithConstraints {
                val maxSheetHeight = maxHeight * 0.9f

                Column(
                    modifier = Modifier.heightIn(max = maxSheetHeight)
                ) {
                    if (state.isLoading) {
                        ReserveScreenShimmer()
                    } else {
                        ReserveScreenContent(
                            state = state,
                            onIntent = viewModel::onIntent
                        )
                    }
                }
            }
        }
    }

    // Lot Size Info Bottom Sheet
    LotSizeInfoBottomSheet(
        isVisible = state.isLotSizeInfoVisible,
        sheetState = lotSizeInfoSheetState,
        lotSizeInfoList = state.lotSizeInfoList,
        onDismiss = {
            viewModel.onIntent(ReserveIntent.OnLotInfoDismissed)
        }
    )

    // Select Voucher Bottom Sheet
    SelectVoucherBottomSheet(
        isVisible = state.isVoucherBottomSheetVisible,
        sheetState = voucherSheetState,
        vouchers = state.availableVouchers,
        onVoucherSelected = { voucher ->
            viewModel.onIntent(ReserveIntent.OnVoucherSelected(voucher))
        },
        onDismiss = {
            viewModel.onIntent(ReserveIntent.OnVoucherBottomSheetDismissed)
        }
    )

    // Select Payment Method Bottom Sheet
    val paymentSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    SelectPaymentMethodBottomSheet(
        isVisible = state.isPaymentSheetVisible,
        sheetState = paymentSheetState,
        paymentCards = state.availablePaymentCards,
        isRegisterCardLoading = state.isRegisterCardLoading,
        onCardSelected = { card ->
            viewModel.onIntent(ReserveIntent.OnPaymentCardSelected(card))
        },
        onAddNewCard = {
            viewModel.onIntent(ReserveIntent.OnAddNewCardClicked)
        },
        onDismiss = {
            viewModel.onIntent(ReserveIntent.OnPaymentSheetDismissed)
        }
    )
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Drag handle
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

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = LessTheme.spacing.medium)
        ) {

        // Header with venue info
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.lotName,
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
                Spacer(modifier = Modifier.width(LessTheme.spacing.xSmall))
                Icon(
                    painter = painterResource(Res.drawable.ic_info_24dp),
                    contentDescription = "Lot size info",
                    tint = LessTheme.colors.textIconsBrand,
                    modifier = Modifier
                        .size(LessTheme.size.medium)
                        .clickable { onIntent(ReserveIntent.OnLotInfoClicked) }
                )
            }
            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))
            Text(
                text = state.pickupTime,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsGrey
            )
            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))
            Text(
                text = state.lotDescription,
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsBlack
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
                    modifier = Modifier.width(48.dp),
                    text = state.quantity.toString(),
                    style = LessTheme.typography.display36Semibold,
                    color = LessTheme.colors.textIconsBlack,
                    textAlign = TextAlign.Center,
                    maxLines = 1
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
            // Venue image
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.elementsSecondaryElement)
            ){
                AsyncImage(
                    model = state.venueLogoUrl,
                    contentDescription = state.venueName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(Res.drawable.ill_venue_placeholder),
                    placeholder = painterResource(Res.drawable.ill_venue_placeholder)
                )
            }

            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.venueName,
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

        // Select voucher row — only show if vouchers available
        if (state.availableVouchers.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(ReserveIntent.OnSelectVoucherClicked) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gift icon
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(LessTheme.radius.small))
                        .background(LessTheme.colors.elementsSecondaryElement)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_gift_24dp),
                        tint = LessTheme.colors.textIconsBrand,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = ""
                    )
                }

                Spacer(modifier = Modifier.width(LessTheme.spacing.xSmall))

                Text(
                    text = state.selectedVoucher?.name
                        ?: stringResource(Res.string.reserve_select_voucher),
                    style = LessTheme.typography.body16Medium,
                    color = LessTheme.colors.textIconsBlack,
                    modifier = Modifier.weight(1f)
                )

                if (state.selectedVoucher != null) {
                    // X to remove voucher
                    Icon(
                        painter = painterResource(Res.drawable.ic_close_24dp),
                        contentDescription = "Remove voucher",
                        tint = LessTheme.colors.textIconsGrey,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onIntent(ReserveIntent.OnVoucherRemoved) }
                    )
                } else {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_chevron_down24dp),
                        contentDescription = "Select voucher",
                        tint = LessTheme.colors.textIconsBrand,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        }

        // Voucher warning
        if (state.voucherWarning != null) {
            Text(
                text = state.voucherWarning,
                style = LessTheme.typography.body14Medium,
                color = LessTheme.colors.textIconsError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))
        }

        // Price breakdown
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Price per piece
            PriceRow(
                label = stringResource(Res.string.reserve_price_per_piece),
                value = "${state.pricePerPiece.formatPrice()} ₼"
            )

            // Discount row — only when voucher selected
            if (state.discount > 0.0) {
                Spacer(modifier = Modifier.height(LessTheme.spacing.small))

                PriceRow(
                    label = stringResource(Res.string.voucher_discount),
                    value = "${state.discount.formatPrice()} ₼"
                )
            }

            // Service fee — show row if rate is configured
            if (state.serviceFeeRate > 0.0) {
                Spacer(modifier = Modifier.height(LessTheme.spacing.small))

                PriceRow(
                    label = stringResource(Res.string.reserve_service_fee),
                    value = "${state.serviceFee.formatPrice()} ₼"
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            // Subtotal — with strikethrough if discount applied
            if (state.discount > 0.0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.reserve_subtotal),
                        style = LessTheme.typography.body16Medium,
                        color = LessTheme.colors.textIconsGrey
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${state.subtotalBeforeDiscount.formatPrice()} ₼",
                            style = LessTheme.typography.body16Medium.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = LessTheme.colors.textIconsGrey
                        )
                        Text(
                            text = "${state.subtotal.formatPrice()} ₼",
                            style = LessTheme.typography.body16Medium,
                            color = LessTheme.colors.textIconsBlack
                        )
                    }
                }
            } else {
                PriceRow(
                    label = stringResource(Res.string.reserve_subtotal),
                    value = "${state.subtotal.formatPrice()} ₼"
                )
            }
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Payment method selector
        if (state.isPaymentLoading) {
            PaymentMethodShimmer()
        } else {
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
                        // Payment icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(LessTheme.radius.small))
                                .background(LessTheme.colors.elementsSecondaryElement),
                            contentAlignment = Alignment.Center
                        ) {
                            when (state.selectedPaymentCard?.type) {
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
                                else -> Icon(
                                    imageVector = vectorResource(Res.drawable.ic_payment_card_24dp),
                                    contentDescription = null,
                                    tint = LessTheme.colors.textIconsGrey,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

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
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        } // end scrollable content

        // Reserve and pay button — pinned at bottom, safe from nav bar
        DsButton(
            text = stringResource(Res.string.reserve_and_pay),
            onClick = { onIntent(ReserveIntent.OnPlaceOrderClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium)
                .padding(bottom = 8.dp)
                .navigationBarsPadding(),
            variant = ButtonVariant.Primary,
            size = ButtonSize.Large,
            enabled = !state.isLoading,
            isLoading = state.isLoading
        )
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

