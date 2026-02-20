package az.less.mobile.presentation.client.main.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.ClientRoute
import az.less.mobile.presentation.client.main.offers.components.CategoryCard
import az.less.mobile.presentation.client.main.search.components.SearchHeader
import az.less.mobile.presentation.client.main.search.components.SearchInputBar
import az.less.mobile.presentation.client.main.search.components.SearchOfferCard
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful SearchScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    
    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SearchSideEffect.NavigateToCategoryOffers -> {
                navController.navigate(
                    ClientRoute.CategoryOffers(
                        categoryId = sideEffect.categoryId,
                        categoryType = sideEffect.categoryType,
                        categoryTitle = sideEffect.categoryTitle
                    )
                )
            }
            is SearchSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is SearchSideEffect.ShowError -> {
                // Show error snackbar
            }
            is SearchSideEffect.NavigateToMap -> {
                navController.navigate(
                    ClientRoute.Explore,
                    navOptions {
                        popUpTo<ClientRoute.Search> {
                            inclusive = true
                        }
                    }
                )
            }
        }
    }
    
    // Render the stateless UI
    SearchScreenContent(
        state = state,
        onIntent = viewModel::onIntent,
        onBackClick = { navController.popBackStack() }
    )
}

/**
 * Stateless SearchScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun SearchScreenContent(
    state: SearchState,
    onIntent: (SearchIntent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .clip(RoundedCornerShape(36.dp))
    ) {
        // Header Section - matching OffersScreen spacing
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        SearchHeader(
            onBackClick = {
                onIntent(SearchIntent.OnBackClicked)
            },
            modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium+LessTheme.spacing.xxSmall))
        
        // Search Input Bar
        SearchInputBar(
            searchQuery = state.searchQuery,
            onSearchQueryChanged = { query ->
                onIntent(
                    SearchIntent.OnSearchQueryChanged(
                        query
                    )
                )
            },
            onMapClicked = {
                onIntent(SearchIntent.OnMapClicked)
            },
            modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
        )
        
        // Show categories only when there are no search results (offers list is empty)
        if (state.offers.isEmpty()) {
            // Categories Grid - Fixed 2 rows
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = LessTheme.spacing.medium), // Adjust height to fit 2 rows
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall),
                contentPadding = PaddingValues(horizontal = LessTheme.spacing.medium)
            ) {
                items(
                    items = state.categories,
                    key = { category -> category.id }
                ) { category ->
                    if (category.testImage != null) {
                        CategoryCard(
                            title = category.title,
                            onClick = {
                                onIntent(
                                    SearchIntent.OnCategorySelected(
                                        category.id
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
        
        // Search Results - LazyColumn for offers
        if (state.offers.isNotEmpty()) {
            Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = LessTheme.spacing.medium,
                    end = LessTheme.spacing.medium,
                    bottom = LessTheme.spacing.medium,
                ),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
            ) {
                items(
                    items = state.offers,
                    key = { offer -> offer.id }
                ) { offer ->
                    SearchOfferCard(
                        offer = offer,
                        onClick = {
                            onIntent(
                                SearchIntent.OnOfferClicked(
                                    offer.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

