package az.less.mobile.presentation.client.main.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsToolBar
import az.less.mobile.navigation.ClientRoute
import az.less.mobile.presentation.client.main.favorites.components.FavoriteMerchantCard
import az.less.mobile.presentation.client.main.favorites.components.FavoritesEmptyState
import az.less.mobile.presentation.client.main.favorites.components.FavoritesNotLoggedInState
import az.less.mobile.presentation.client.main.favorites.components.FavoritesScreenShimmer
import az.less.mobile.presentation.client.main.favorites.models.FavoriteMerchant
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.saved_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    val favorites = viewModel.favorites.collectAsLazyPagingItems()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is FavoritesSideEffect.NavigateToMerchantDetail -> {
                navController.navigate(ClientRoute.Merchant(merchantId = sideEffect.merchantId))
            }

            is FavoritesSideEffect.NavigateToExplore -> {
                navController.navigate(ClientRoute.Explore)
            }

            is FavoritesSideEffect.NavigateToMore -> {
                navController.navigate(ClientRoute.More)
            }

            is FavoritesSideEffect.ShowError -> {
                // Show error snackbar
            }
        }
    }

    FavoritesScreenContent(
        state = state,
        favorites = favorites,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun FavoritesScreenContent(
    state: FavoritesState,
    favorites: LazyPagingItems<FavoriteMerchant>,
    onIntent: (FavoritesIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = LessTheme.size.large)
    ) {
        DsToolBar(
            title = stringResource(Res.string.saved_title),
            backgroundColor = LessTheme.colors.backgroundSecond
        )

        when {
            !state.isLoggedIn -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = LessTheme.spacing.medium),
                    contentAlignment = Alignment.Center
                ) {
                    FavoritesNotLoggedInState(
                        onSignInClick = {
                            onIntent(FavoritesIntent.OnSignInClicked)
                        }
                    )
                }
            }

            favorites.loadState.refresh is LoadState.Loading -> {
                FavoritesScreenShimmer(
                    modifier = Modifier.fillMaxSize()
                )
            }

            favorites.itemCount == 0 && favorites.loadState.refresh is LoadState.NotLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = LessTheme.spacing.medium),
                    contentAlignment = Alignment.Center
                ) {
                    FavoritesEmptyState(
                        onExploreNewVenuesClick = {
                            onIntent(FavoritesIntent.OnExploreNewVenuesClicked)
                        }
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = LessTheme.spacing.medium
                    ),
                    verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
                ) {
                    items(
                        count = favorites.itemCount,
                        key = favorites.itemKey { it.id }
                    ) { index ->
                        val merchant = favorites[index]
                        if (merchant != null) {
                            FavoriteMerchantCard(
                                merchant = merchant,
                                onClick = {
                                    onIntent(FavoritesIntent.OnMerchantClicked(merchant.id))
                                }
                            )
                        }
                    }

                    if (favorites.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(LessTheme.spacing.medium),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = LessTheme.colors.textIconsBrand
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
