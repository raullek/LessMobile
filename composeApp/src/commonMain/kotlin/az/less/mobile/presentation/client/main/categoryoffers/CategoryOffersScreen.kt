package az.less.mobile.presentation.client.main.categoryoffers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.HomeScreens
import az.less.mobile.presentation.client.reserve.ReserveScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful CategoryOffersScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryOffersScreen(
    viewModel: CategoryOffersViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()
    
    // Reserve bottom sheet state
    var isReserveBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedOfferItem by remember { mutableStateOf<az.less.mobile.presentation.client.main.offers.models.OfferItem?>(null) }
    val reserveSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CategoryOffersSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is CategoryOffersSideEffect.NavigateToReserve -> {
                selectedOfferItem = sideEffect.offerItem
                isReserveBottomSheetVisible = true
                scope.launch {
                    reserveSheetState.expand()
                }
            }
            is CategoryOffersSideEffect.ShowError -> {
                // Show error snackbar
            }
        }
    }
    
    // Render the stateless UI
    CategoryOffersScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
    
    // Reserve Bottom Sheet
    if (isReserveBottomSheetVisible && selectedOfferItem != null) {
        ReserveScreen(
            isVisible = isReserveBottomSheetVisible,
            sheetState = reserveSheetState,
            viewModel = koinViewModel(),
            onDismiss = {
                scope.launch {
                    reserveSheetState.hide()
                }.invokeOnCompletion {
                    isReserveBottomSheetVisible = false
                }
            },
            onOrderPlaced = { orderInfo ->
                // Navigate to Order Accepted screen
                navController.navigate(
                    HomeScreens.OrderAccepted.createRoute(
                        orderNumber = orderInfo.orderNumber,
                        venueName = orderInfo.venueName,
                        pickupTime = orderInfo.pickupTime
                    )
                )
            }
        )
    }
}

/**
 * Stateless CategoryOffersScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun CategoryOffersScreenContent(
    state: CategoryOffersState,
    onIntent: (CategoryOffersIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .clip(RoundedCornerShape(36.dp))
    ) {
        // Header Section - matching SearchScreen spacing
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        _root_ide_package_.az.less.mobile.presentation.client.main.categoryoffers.components.CategoryOffersHeader(
            title = state.categoryTitle,
            onBackClick = { onIntent(CategoryOffersIntent.OnBackClicked) },
            modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium + LessTheme.spacing.xxSmall))
        
        if (state.isEmpty) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No offers available",
                    style = LessTheme.typography.body16Regular,
                    color = LessTheme.colors.textIconsGrey,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // List of Offers
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(
                    horizontal = LessTheme.spacing.medium,
                    vertical = LessTheme.spacing.medium
                )
            ) {
                // Offers Vertical List
                items(
                    items = state.offers,
                    key = { offer -> offer.id }
                ) { offer ->
                    _root_ide_package_.az.less.mobile.presentation.client.main.categoryoffers.components.VerticalOfferCard(
                        item = offer,
                        onClick = {
                            onIntent(
                                CategoryOffersIntent.OnOfferItemClicked(
                                    offer.id
                                )
                            )
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
                }
            }
        }
    }
}

