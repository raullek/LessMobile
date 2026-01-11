package az.less.mobile.presentation.client.main.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsToolBar
import az.less.mobile.navigation.ClientRoute
import az.less.mobile.presentation.client.main.saved.components.SavedEmptyState
import az.less.mobile.presentation.client.main.saved.components.SavedMerchantCard
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful SavedScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun SavedScreen(
    viewModel: SavedViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SavedSideEffect.NavigateToMerchantDetail -> {
                navController.navigate(ClientRoute.Merchant(merchantId = sideEffect.merchantId))
            }
            is SavedSideEffect.NavigateToExplore -> {
                navController.navigate(ClientRoute.Explore)
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = LessTheme.size.large) // Space for bottom nav bar
    ) {
        // Fixed Toolbar with title
        DsToolBar(
            title = "Favourites",
            backgroundColor = LessTheme.colors.backgroundSecond
        )

        if (state.isEmpty) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LessTheme.spacing.medium),
                contentAlignment = Alignment.Center
            ) {
                SavedEmptyState(
                    onExploreNewVenuesClick = {
                        onIntent(SavedIntent.OnExploreNewVenuesClicked)
                    }
                )
            }
        } else {
            // List of Saved Merchants
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = LessTheme.spacing.medium
                ),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
            ) {
                // Saved Merchants
                items(
                    items = state.savedMerchants,
                    key = { merchant -> merchant.id }
                ) { merchant ->
                    SavedMerchantCard(
                        merchant = merchant,
                        onClick = {
                            onIntent(SavedIntent.OnMerchantClicked(merchant.id))
                        }
                    )
                }
            }
        }
    }
}
