package az.less.mobile.presentation.merchant.places.edit

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.CellType
import az.less.designsystem.components.DsCell
import az.less.mobile.navigation.MerchantScreens
import az.less.mobile.presentation.merchant.places.edit.components.EditMerchantProfileHeroSection
import az.less.mobile.presentation.merchant.places.edit.components.EditMerchantProfileInfoSection
import az.less.mobile.presentation.merchant.places.edit.components.EditMerchantProfileContactSection
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful EditMerchantProfileScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun EditMerchantProfileScreen(
    branchId: String? = null,
    viewModel: EditMerchantProfileViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    // Initialize ViewModel with branchId
    LaunchedEffect(branchId) {
        viewModel.initialize(branchId)
    }

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is EditMerchantProfileSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is EditMerchantProfileSideEffect.NavigateToImagePicker -> {
                // TODO: Open image picker
            }
            is EditMerchantProfileSideEffect.NavigateToLogoPicker -> {
                // TODO: Open logo picker
            }
            is EditMerchantProfileSideEffect.NavigateToLotsPicker -> {
                // TODO: Open lots image picker
            }
            is EditMerchantProfileSideEffect.ShowError -> {
                // Show error snackbar
            }
            is EditMerchantProfileSideEffect.BranchCreated -> {
                navController.navigate(MerchantScreens.BranchVerification.route)
            }
            is EditMerchantProfileSideEffect.BranchUpdated -> {
                navController.popBackStack()
            }
        }
    }

    // Render the stateless UI
    EditMerchantProfileScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless EditMerchantProfileScreen UI implementation
 * Based on MerchantProfileScreen structure with edit icons
 */
@Composable
fun EditMerchantProfileScreenContent(
    state: EditMerchantProfileState,
    onIntent: (EditMerchantProfileIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LessTheme.colors.backgroundSecond),
            contentPadding = PaddingValues(
                bottom = LessTheme.spacing.xLarge + 80.dp // Space for bottom button
            )
        ) {
            // Hero Section with overlapping logo and edit icons
            item(key = "hero_section") {
                EditMerchantProfileHeroSection(
                    venueImageUrl = state.venueImageUrl,
                    logoUrl = state.logoUrl,
                    onBackClick = { onIntent(EditMerchantProfileIntent.OnBackClick) },
                    onVenueImageEditClick = { onIntent(EditMerchantProfileIntent.OnVenueImageEditClick) },
                    onLogoEditClick = { onIntent(EditMerchantProfileIntent.OnLogoEditClick) }
                )
            }

            // Merchant Profile Info Section
            item(key = "profile_info") {
                EditMerchantProfileInfoSection(
                    venueName = state.venueName,
                    description = state.description,
                    onNameEditClick = { onIntent(EditMerchantProfileIntent.OnNameEditClick) },
                    onDescriptionChanged = { onIntent(EditMerchantProfileIntent.OnDescriptionChanged(it)) }
                )
            }

            // Contact Section
            item(key = "contact_section") {
                EditMerchantProfileContactSection(
                    phoneNumber = state.phoneNumber,
                    location = state.location,
                    onPhoneEditClick = { onIntent(EditMerchantProfileIntent.OnPhoneEditClick) },
                    onLocationEditClick = { onIntent(EditMerchantProfileIntent.OnLocationEditClick) }
                )
            }

            // Toggle Section (using DsCell like in More screen)
            item(key = "toggle_section") {
                DsCell(
                    title = "Manage this location from this email",
                    type = CellType.Toggle(
                        checked = state.manageFromEmail,
                        onCheckedChange = { onIntent(EditMerchantProfileIntent.OnManageFromEmailToggled(it)) }
                    ),
                    showDivider = false,
                    modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                )
            }
        }

        // Bottom Create Branch Button
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
                text = if (state.branchId == null) "Create branch" else "Update branch",
                onClick = { onIntent(EditMerchantProfileIntent.OnCreateBranchClick) },
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large
            )
        }
    }
}

