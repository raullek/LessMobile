package az.less.mobile.presentation.main.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.main.saved.components.FavoriteItemCard
import az.less.mobile.presentation.main.saved.components.SavedEmptyState
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
            is SavedSideEffect.NavigateToItemDetail -> {
                // Handle navigation to item detail
                // navController.navigate("item_detail/${sideEffect.itemId}")
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
