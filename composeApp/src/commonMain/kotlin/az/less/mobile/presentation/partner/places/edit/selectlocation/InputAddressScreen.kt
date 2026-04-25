package az.less.mobile.presentation.partner.places.edit.selectlocation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.DsToolBar
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful InputAddressScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun InputAddressScreen(
    viewModel: InputAddressViewModel = koinViewModel(),
    navController: NavController,
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is InputAddressSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is InputAddressSideEffect.AddressSelected -> {
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set("input_latitude", sideEffect.latitude)
                    set("input_longitude", sideEffect.longitude)
                    set("input_address", sideEffect.address)
                }
                navController.popBackStack()
            }
            is InputAddressSideEffect.ShowError -> {}

        }
    }

    InputAddressScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless InputAddressScreen UI implementation
 */
@Composable
fun InputAddressScreenContent(
    state: InputAddressState,
    onIntent: (InputAddressIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            DsToolBar(
                title = "Enter address",
                onBackClick = { onIntent(InputAddressIntent.OnBackClick) }
            )
        },
        containerColor = LessTheme.colors.backgroundPrimary
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search input field
            DsTextField(
                value = state.searchQuery,
                onValueChange = { onIntent(InputAddressIntent.OnSearchQueryChanged(it)) },
                placeholder = "Search address...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LessTheme.spacing.medium)
                    .padding(top = LessTheme.spacing.small),
                onEndIconClick = {
                    onIntent(InputAddressIntent.OnClearSearch)
                }
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            // Loading indicator
            if (state.isSearching) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LessTheme.spacing.medium),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = LessTheme.colors.elementsPrimaryBrand
                    )
                }
            }

            // Search results
            if (state.searchResults.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = state.searchResults,
                        key = { it.id }
                    ) { suggestion ->
                        AddressSuggestionItem(
                            suggestion = suggestion,
                            onClick = { onIntent(InputAddressIntent.OnAddressSelected(suggestion)) }
                        )
                    }
                }
            }

            // Empty state hint
            if (state.searchQuery.isEmpty() && state.searchResults.isEmpty() && !state.isSearching) {
                Text(
                    text = "Start typing to search for an address",
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LessTheme.spacing.medium)
                )
            }

            // Error state
            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LessTheme.spacing.medium)
                )
            }

            // No results state
            if (state.searchQuery.length >= 3 && state.searchResults.isEmpty() && !state.isSearching && state.errorMessage == null) {
                Text(
                    text = "No addresses found",
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LessTheme.spacing.medium)
                )
            }
        }
    }
}

/**
 * Address suggestion item component
 */
@Composable
private fun AddressSuggestionItem(
    suggestion: AddressSuggestion,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(LessTheme.colors.backgroundPrimary)
            .padding(
                horizontal = LessTheme.spacing.medium,
                vertical = LessTheme.spacing.small
            )
    ) {
        Text(
            text = suggestion.address,
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsBlack
        )

        suggestion.subtitle?.let { subtitle ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsSecondary
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = LessTheme.spacing.small),
            color = LessTheme.colors.borderPrimary
        )
    }
}
