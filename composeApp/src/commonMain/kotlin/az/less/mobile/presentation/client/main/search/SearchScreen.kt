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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import androidx.paging.compose.collectAsLazyPagingItems
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.ClientRoute
import az.less.mobile.presentation.client.main.offers.components.CategoryCard
import az.less.mobile.presentation.client.main.search.components.SearchHeader
import az.less.mobile.presentation.client.main.search.components.SearchInputBar
import az.less.mobile.presentation.client.main.search.components.SearchVenueCard
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    val lazyPagingItems = viewModel.searchResults.collectAsLazyPagingItems()

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
            is SearchSideEffect.NavigateToMerchantProfile -> {
                navController.navigate(
                    ClientRoute.Merchant(merchantId = sideEffect.merchantId)
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .clip(RoundedCornerShape(36.dp))
    ) {
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        SearchHeader(
            onBackClick = { viewModel.onIntent(SearchIntent.OnBackClicked) },
            modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium + LessTheme.spacing.xxSmall))

        SearchInputBar(
            searchQuery = state.searchQuery,
            onSearchQueryChanged = { query ->
                viewModel.onIntent(SearchIntent.OnSearchQueryChanged(query))
            },
            onMapClicked = { viewModel.onIntent(SearchIntent.OnMapClicked) },
            modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
        )

        val hasResults = state.searchQuery.isNotBlank() && lazyPagingItems.itemCount > 0

        if (!hasResults) {
            // Categories Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = LessTheme.spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall),
                contentPadding = PaddingValues(horizontal = LessTheme.spacing.medium)
            ) {
                items(
                    items = state.categories,
                    key = { category -> category.id }
                ) { category ->
                    CategoryCard(
                        title = category.title,
                        imageUrl = category.imageUrl,
                        onClick = {
                            viewModel.onIntent(SearchIntent.OnCategorySelected(category.id))
                        },
                    )
                }
            }
        }

        if (state.searchQuery.isNotBlank()) {
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
                    count = lazyPagingItems.itemCount,
                    key = { index -> lazyPagingItems[index]?.id ?: index }
                ) { index ->
                    val venue = lazyPagingItems[index]
                    if (venue != null) {
                        SearchVenueCard(
                            venue = venue,
                            onClick = {
                                viewModel.onIntent(SearchIntent.OnVenueClicked(venue.id))
                            }
                        )
                    }
                }
            }
        }
    }
}
