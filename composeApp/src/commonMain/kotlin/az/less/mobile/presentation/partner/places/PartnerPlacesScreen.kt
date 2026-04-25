package az.less.mobile.presentation.partner.places

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsListBottomSheet
import az.less.designsystem.components.ListBottomSheetItem
import az.less.mobile.navigation.PartnerRoute
import az.less.mobile.presentation.partner.places.components.BranchCard
import az.less.mobile.presentation.partner.places.components.PlacesEmptyState
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_edit_24dp
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import lessmobile.composeapp.generated.resources.places_add_branch
import lessmobile.composeapp.generated.resources.places_edit_merch_details
import lessmobile.composeapp.generated.resources.places_edit_users
import lessmobile.composeapp.generated.resources.places_edit_venue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful PartnerPlacesScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun PartnerPlacesScreen(
    viewModel: PartnerPlacesViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PartnerPlacesSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is PartnerPlacesSideEffect.NavigateToEditBranch -> {
                navController.navigate(PartnerRoute.EditProfile(venueData = sideEffect.branch.encode()))
            }
            is PartnerPlacesSideEffect.NavigateToEditUsers -> {
                navController.navigate(
                    PartnerRoute.BranchUsers(
                        venueId = sideEffect.branch.id,
                        venueName = sideEffect.branch.name
                    )
                )
            }
            is PartnerPlacesSideEffect.NavigateToAddBranch -> {
                navController.navigate(PartnerRoute.EditProfile())
            }
            is PartnerPlacesSideEffect.ShowError -> {
                // Show error snackbar or dialog
            }
        }
    }

    // Render the stateless UI
    PartnerPlacesScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless PartnerPlacesScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerPlacesScreenContent(
    state: PartnerPlacesState,
    onIntent: (PartnerPlacesIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onIntent(PartnerPlacesIntent.OnRefresh) },
            modifier = Modifier.weight(1f)
        ) {
            val listState = rememberLazyListState()

            // Trigger load more when near the end of the list
            val shouldLoadMore = remember {
                derivedStateOf {
                    val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    val totalItems = listState.layoutInfo.totalItemsCount
                    lastVisibleItem >= totalItems - 3 && totalItems > 0
                }
            }

            LaunchedEffect(shouldLoadMore.value) {
                if (shouldLoadMore.value && state.hasNextPage && !state.isLoadingMore) {
                    onIntent(PartnerPlacesIntent.OnLoadMore)
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = LessTheme.spacing.large,
                    bottom = LessTheme.size.huge + LessTheme.spacing.xLarge
                ),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
            ) {

            when {
                state.isLoading -> {
                    // Loading state
                    item(key = "loading") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = LessTheme.spacing.xLarge),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = LessTheme.colors.elementsPrimaryBrand
                            )
                        }
                    }
                }
                state.isEmpty -> {
                    // Empty state
                    item(key = "empty") {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(top = LessTheme.size.huge),
                            contentAlignment = Alignment.Center
                        ) {
                            PlacesEmptyState(
                                onAddBranchClick = { onIntent(PartnerPlacesIntent.OnAddBranchClick) }
                            )
                        }
                    }
                }
                else -> {
                    // Branch list
                    items(
                        items = state.branches,
                        key = { branch -> branch.id }
                    ) { branch ->
                        BranchCard(
                            branch = branch,
                            onClick = { onIntent(PartnerPlacesIntent.OnBranchClick(branch.id)) },
                            onEditClick = { onIntent(PartnerPlacesIntent.OnEditBranchClick(branch.id)) },
                            modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                        )
                    }

                    // Loading more indicator
                    if (state.isLoadingMore) {
                        item(key = "loading_more") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LessTheme.spacing.medium),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = LessTheme.colors.elementsPrimaryBrand,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    // Add Branch Button as last list item
                    item(key = "add_branch_button") {
                        DsButton(
                            text = stringResource(Res.string.places_add_branch),
                            onClick = { onIntent(PartnerPlacesIntent.OnAddBranchClick) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = LessTheme.spacing.medium),
                            variant = ButtonVariant.Primary,
                            size = ButtonSize.Large
                        )
                    }
                }
            }
            }
        }
    }

    // Edit Merch Details Bottom Sheet
    if (state.showEditBottomSheet) {
        val editIcon = painterResource(Res.drawable.ic_edit_24dp)
        val chevronIcon = painterResource(Res.drawable.ic_chevron_right_24dp)

        val editItems = listOf(
            ListBottomSheetItem(
                id = "edit_venue",
                title = stringResource(Res.string.places_edit_venue),
                icon = editIcon
            ),
            ListBottomSheetItem(
                id = "edit_users",
                title = stringResource(Res.string.places_edit_users),
                icon = editIcon
            )
        )

        DsListBottomSheet(
            title = stringResource(Res.string.places_edit_merch_details),
            items = editItems,
            onItemClick = { itemId ->
                when (itemId) {
                    "edit_venue" -> onIntent(PartnerPlacesIntent.OnEditVenueClick)
                    "edit_users" -> onIntent(PartnerPlacesIntent.OnEditUsersClick)
                }
            },
            onDismiss = { onIntent(PartnerPlacesIntent.OnDismissEditBottomSheet) },
            chevronIcon = chevronIcon
        )
    }
}
