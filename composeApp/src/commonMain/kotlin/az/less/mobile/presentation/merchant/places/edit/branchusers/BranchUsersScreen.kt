package az.less.mobile.presentation.merchant.places.edit.branchusers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsToolBar
import az.less.mobile.navigation.MerchantRoute
import az.less.mobile.presentation.merchant.places.edit.branchusers.components.BranchUsersEmptyState
import az.less.mobile.presentation.merchant.places.edit.branchusers.components.UserCell
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.branch_users_add_other
import lessmobile.composeapp.generated.resources.branch_users_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful BranchUsersScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun BranchUsersScreen(
    viewModel: BranchUsersViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is BranchUsersSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is BranchUsersSideEffect.NavigateToAddUser -> {
                val userNumber = state.users.size + 1
                navController.navigate(MerchantRoute.AddBranchUser(userNumber = userNumber))
            }
            is BranchUsersSideEffect.NavigateToEditUser -> {
                navController.navigate(
                    MerchantRoute.AddBranchUser(
                        userNumber = sideEffect.userNumber,
                        user = sideEffect.user.encode()
                    )
                )
            }
            is BranchUsersSideEffect.ShowError -> {
                // Show error snackbar or dialog
            }
        }
    }

    // Render the stateless UI
    BranchUsersScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless BranchUsersScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun BranchUsersScreenContent(
    state: BranchUsersState,
    onIntent: (BranchUsersIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundPrimary)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Fixed Toolbar with branch name
        DsToolBar(
            title = stringResource(Res.string.branch_users_title, state.branchName),
            onBackClick = { onIntent(BranchUsersIntent.OnBackClick) },
            backgroundColor = LessTheme.colors.backgroundPrimary
        )

        // Main content
        Box(modifier = Modifier.weight(1f)) {
            when {
                state.isLoading -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = LessTheme.colors.elementsPrimaryBrand
                        )
                    }
                }
                state.isEmpty -> {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = LessTheme.size.huge),
                        contentAlignment = Alignment.Center
                    ) {
                        BranchUsersEmptyState(
                            onAddUserClick = { onIntent(BranchUsersIntent.OnAddUserClick) }
                        )
                    }
                }
                else -> {
                    // Users list with "Add other user" text below
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = LessTheme.spacing.medium),
                        contentPadding = PaddingValues(
                            top = LessTheme.spacing.small,
                            bottom = LessTheme.spacing.xLarge
                        ),
                        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
                    ) {
                        items(
                            items = state.users,
                            key = { user -> user.id }
                        ) { user ->
                            UserCell(
                                name = user.name,
                                onClick = { onIntent(BranchUsersIntent.OnUserClick(user.id)) }
                            )
                        }

                        // "Add other user" text below the list
                        item {
                            Spacer(modifier = Modifier.height(LessTheme.spacing.small))
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(Res.string.branch_users_add_other),
                                    style = LessTheme.typography.body16Medium,
                                    color = LessTheme.colors.textIconsBrand,
                                    modifier = Modifier.clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = { onIntent(BranchUsersIntent.OnAddUserClick) }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

