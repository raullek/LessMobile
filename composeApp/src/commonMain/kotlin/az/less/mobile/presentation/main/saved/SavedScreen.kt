package az.less.mobile.presentation.main.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.ReserveScreens
import az.less.mobile.presentation.main.saved.components.FavoriteItemCard
import az.less.mobile.presentation.main.saved.components.SavedEmptyState
import az.less.mobile.presentation.reserve.ReserveScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful SavedScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    viewModel: SavedViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()
    
    // Reserve bottom sheet state
    var isReserveBottomSheetVisible by remember { mutableStateOf(false) }
    val reserveSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    
    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SavedSideEffect.NavigateToReserve -> {
                // Open reserve bottom sheet
                isReserveBottomSheetVisible = true
                scope.launch {
                    reserveSheetState.expand()
                }
            }
            is SavedSideEffect.NavigateToExplore -> {
                // Handle navigation to explore/offers tab
                // navController.navigate("offers")
            }
            is SavedSideEffect.ShowError -> {
                // Show error snackbar
            }
        }
    }
    
    // Render the stateless UI
    SavedScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
    
    // Reserve Bottom Sheet
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
                ReserveScreens.OrderAccepted.createRoute(
                    orderNumber = orderInfo.orderNumber,
                    venueName = orderInfo.venueName,
                    pickupTime = orderInfo.pickupTime
                )
            )
        }
    )
}

/**
 * Stateless SavedScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun SavedScreenContent(
    state: SavedState,
    onIntent: (SavedIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = LessTheme.size.large) // Space for bottom nav bar
    ) {
        if (state.isEmpty) {
            // Empty State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SavedEmptyState(
                    onExploreNewVenuesClick = {
                        onIntent(SavedIntent.OnExploreNewVenuesClicked)
                    }
                )
            }
        } else {
            // List of Favorites
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = LessTheme.spacing.medium,
                    vertical = LessTheme.spacing.medium
                )
            ) {
                // Title
                item(key = "title") {
                    Text(
                        text = "Favourites",
                        style = LessTheme.typography.body16Semibold,
                        color = LessTheme.colors.textIconsBlack,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                item(key = "title_spacing") {
                    Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
                }
                
                // Favorite Items
                items(
                    items = state.favoriteItems,
                    key = { item -> item.id }
                ) { item ->
                    FavoriteItemCard(
                        item = item,
                        onClick = {
                            onIntent(SavedIntent.OnItemClicked(item.id))
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
                }
            }
        }
    }
}
