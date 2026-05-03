package az.less.mobile.presentation.merchant.places

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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
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
import az.less.designsystem.components.DsToolBar
import az.less.designsystem.components.ListBottomSheetItem
import az.less.mobile.navigation.MerchantRoute
import az.less.mobile.presentation.merchant.places.components.BranchCard
import az.less.mobile.presentation.merchant.places.components.PlacesEmptyState
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_edit_24dp
import lessmobile.composeapp.generated.resources.ic_eye_24dp
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import lessmobile.composeapp.generated.resources.places_add_branch
import lessmobile.composeapp.generated.resources.places_edit_merch_details
import lessmobile.composeapp.generated.resources.places_edit_users
import lessmobile.composeapp.generated.resources.places_edit_venue
import lessmobile.composeapp.generated.resources.places_preview
import lessmobile.composeapp.generated.resources.places_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful MerchPlacesScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun MerchPlacesScreen(
    viewModel: MerchPlacesViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MerchPlacesSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is MerchPlacesSideEffect.NavigateToEditBranch -> {
                navController.navigate(MerchantRoute.EditProfile(venueData = sideEffect.branch.encode()))
            }
            is MerchPlacesSideEffect.NavigateToEditUsers -> {
                navController.navigate(
                    MerchantRoute.BranchUsers(
                        venueId = sideEffect.branch.id,
                        venueName = sideEffect.branch.name
                    )
                )
            }
            is MerchPlacesSideEffect.NavigateToAddBranch -> {
                navController.navigate(MerchantRoute.EditProfile())
            }
            is MerchPlacesSideEffect.NavigateToPreview -> {
                navController.navigate(MerchantRoute.VenuePreview(venueId = sideEffect.branch.id))
            }
            is MerchPlacesSideEffect.ShowError -> {
                // Show error snackbar or dialog
            }
        }
    }

    // Render the stateless UI
    MerchPlacesScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless MerchPlacesScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun MerchPlacesScreenContent(
    state: MerchPlacesState,
    onIntent: (MerchPlacesIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Fixed Toolbar
        DsToolBar(
            title = stringResource(Res.string.places_title),
            onBackClick = { onIntent(MerchPlacesIntent.OnBackClick) },
            backgroundColor = LessTheme.colors.backgroundSecond
        )

        // Main content
        Box(modifier = Modifier.weight(1f)) {
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
                    onIntent(MerchPlacesIntent.OnLoadMore)
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = LessTheme.spacing.xxxSmall,
                    bottom = if (state.isEmpty) {
                        LessTheme.spacing.xLarge
                    } else {
                        LessTheme.spacing.xLarge + 80.dp // Space for bottom button
                    }
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
                                onAddBranchClick = { onIntent(MerchPlacesIntent.OnAddBranchClick) }
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
                            isDefault = branch.id == state.defaultVenueId,
                            onClick = { onIntent(MerchPlacesIntent.OnBranchClick(branch.id)) },
                            onEditClick = { onIntent(MerchPlacesIntent.OnEditBranchClick(branch.id)) },
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
                }
            }
            }

            // Bottom Add Branch Button - only show when there are branches
            if (!state.isEmpty) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(
                            horizontal = LessTheme.spacing.medium,
                            vertical = LessTheme.spacing.medium
                        )
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    DsButton(
                        text = stringResource(Res.string.places_add_branch),
                        onClick = { onIntent(MerchPlacesIntent.OnAddBranchClick) },
                        modifier = Modifier.fillMaxWidth(),
                        variant = ButtonVariant.Primary,
                        size = ButtonSize.Large
                    )
                }
            }
        }
    }

    // Edit Merch Details Bottom Sheet
    if (state.showEditBottomSheet) {
        val editIcon = painterResource(Res.drawable.ic_edit_24dp)
        val eyeIcon = painterResource(Res.drawable.ic_eye_24dp)
        val chevronIcon = painterResource(Res.drawable.ic_chevron_right_24dp)

        val editItems = listOf(
            ListBottomSheetItem(
                id = "preview",
                title = stringResource(Res.string.places_preview),
                icon = eyeIcon
            ),
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
                    "edit_venue" -> onIntent(MerchPlacesIntent.OnEditVenueClick)
                    "edit_users" -> onIntent(MerchPlacesIntent.OnEditUsersClick)
                    "preview" -> onIntent(MerchPlacesIntent.OnPreviewClick)
                }
            },
            onDismiss = { onIntent(MerchPlacesIntent.OnDismissEditBottomSheet) },
            chevronIcon = chevronIcon
        )
    }
}
