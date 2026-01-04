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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import az.less.designsystem.components.DsToolBar
import az.less.mobile.navigation.MerchantRoute
import az.less.mobile.presentation.merchant.places.components.BranchCard
import az.less.mobile.presentation.merchant.places.components.PlacesEmptyState
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
                navController.navigate(MerchantRoute.EditProfile(branchId = sideEffect.branchId))
            }
            is MerchPlacesSideEffect.NavigateToAddBranch -> {
                navController.navigate(MerchantRoute.EditProfile())
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
            title = "Places",
            onBackClick = { onIntent(MerchPlacesIntent.OnBackClick) },
            backgroundColor = LessTheme.colors.backgroundSecond
        )

        // Main content
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
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
                            onClick = { onIntent(MerchPlacesIntent.OnBranchClick(branch.id)) },
                            onEditClick = { onIntent(MerchPlacesIntent.OnEditBranchClick(branch.id)) },
                            modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                        )
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
                        text = "Add branch",
                        onClick = { onIntent(MerchPlacesIntent.OnAddBranchClick) },
                        modifier = Modifier.fillMaxWidth(),
                        variant = ButtonVariant.Primary,
                        size = ButtonSize.Large
                    )
                }
            }
        }
    }
}
