package az.less.mobile.presentation.partner.places.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.AnimatedToast
import az.less.designsystem.components.ToastType
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.CellType
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsCell
import az.less.mobile.navigation.PartnerRoute
import az.less.mobile.presentation.partner.places.edit.components.EditBranchAdditionalSections
import az.less.mobile.presentation.partner.places.edit.components.EditMerchDetailsBottomSheet
import az.less.mobile.presentation.partner.places.edit.components.EditMerchantProfileContactSection
import az.less.mobile.presentation.partner.places.edit.components.EditMerchantProfileHeroSection
import az.less.mobile.presentation.partner.places.edit.components.EditMerchantProfileInfoSection
import az.less.mobile.presentation.partner.places.edit.components.EditPhoneNumberBottomSheet
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.edit_profile_create_branch
import lessmobile.composeapp.generated.resources.edit_profile_error_location_required
import lessmobile.composeapp.generated.resources.edit_profile_error_name_required
import lessmobile.composeapp.generated.resources.edit_profile_error_phone_required
import lessmobile.composeapp.generated.resources.edit_profile_make_me_merchant
import lessmobile.composeapp.generated.resources.edit_profile_update_branch
import lessmobile.composeapp.generated.resources.edit_profile_venue_created
import lessmobile.composeapp.generated.resources.edit_profile_venue_updated
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful EditMerchantProfileScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun EditMerchantProfileScreen(
    venueData: String? = null,
    viewModel: EditMerchantProfileViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    // Toast local state
    var toastVisible by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.Success) }
    val coroutineToastScope = rememberCoroutineScope()

    // Resolve localized validation/success messages
    val validationNameRequired = stringResource(Res.string.edit_profile_error_name_required)
    val validationPhoneRequired = stringResource(Res.string.edit_profile_error_phone_required)
    val validationLocationRequired = stringResource(Res.string.edit_profile_error_location_required)
    val successVenueCreated = stringResource(Res.string.edit_profile_venue_created)
    val successVenueUpdated = stringResource(Res.string.edit_profile_venue_updated)

    // Initialize ViewModel with venue data
    LaunchedEffect(venueData) {
        viewModel.initialize(venueData)
    }

    // Listen for location selection result from SelectBranchLocationOnMapScreen
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getStateFlow<Double?>("selected_latitude", null)?.collect { latitude ->
            val longitude = savedStateHandle.get<Double>("selected_longitude")
            val address = savedStateHandle.get<String>("selected_address")
            if (latitude != null && longitude != null && address != null) {
                viewModel.onIntent(
                    EditMerchantProfileIntent.OnLocationSelected(latitude, longitude, address)
                )
                // Clear the saved state after handling
                savedStateHandle.remove<Double>("selected_latitude")
                savedStateHandle.remove<Double>("selected_longitude")
                savedStateHandle.remove<String>("selected_address")
            }
        }
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
            is EditMerchantProfileSideEffect.NavigateToLocationPicker -> {
                navController.navigate(
                    PartnerRoute.SelectBranchLocation(
                        latitude = sideEffect.latitude,
                        longitude = sideEffect.longitude,
                        address = sideEffect.address
                    )
                )
            }
            is EditMerchantProfileSideEffect.BranchCreated -> {
                navController.navigate(PartnerRoute.BranchVerification)
            }
            is EditMerchantProfileSideEffect.BranchUpdated -> {
                navController.popBackStack()
            }
            is EditMerchantProfileSideEffect.ShowToast -> {
                val resolvedMessage = when (sideEffect.message) {
                    EditMerchantProfileViewModel.VALIDATION_NAME_REQUIRED -> validationNameRequired
                    EditMerchantProfileViewModel.VALIDATION_PHONE_REQUIRED -> validationPhoneRequired
                    EditMerchantProfileViewModel.VALIDATION_LOCATION_REQUIRED -> validationLocationRequired
                    EditMerchantProfileViewModel.SUCCESS_VENUE_CREATED -> successVenueCreated
                    EditMerchantProfileViewModel.SUCCESS_VENUE_UPDATED -> successVenueUpdated
                    else -> sideEffect.message // API error messages as-is
                }
                toastMessage = resolvedMessage
                toastType = sideEffect.type
                toastVisible = true
                coroutineToastScope.launch {
                    delay(3000)
                    toastVisible = false
                }
            }
        }
    }

    // Render the stateless UI
    Box(modifier = Modifier.fillMaxSize()) {
        EditMerchantProfileScreenContent(
            state = state,
            onIntent = viewModel::onIntent
        )

        // Toast
        AnimatedToast(
            visible = toastVisible,
            title = toastMessage,
            type = toastType,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

/**
 * Stateless EditMerchantProfileScreen UI implementation
 * Based on MerchantProfileScreen structure with edit icons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMerchantProfileScreenContent(
    state: EditMerchantProfileState,
    onIntent: (EditMerchantProfileIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Bottom sheet states
    val editMerchDetailsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val editPhoneNumberSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // Venue Image Picker
    Box {
        if (state.showVenueImagePicker) {
            GalleryPickerLauncher(
                onPhotosSelected = { photos ->
                    photos.firstOrNull()?.let { photo ->
                        coroutineScope.launch {
                            val bytes = photo.loadBytes()
                            if (bytes != null) {
                                onIntent(EditMerchantProfileIntent.OnVenueImageSelected(bytes))
                            } else {
                                onIntent(EditMerchantProfileIntent.OnVenueImagePickerDismiss)
                            }
                        }
                    } ?: onIntent(EditMerchantProfileIntent.OnVenueImagePickerDismiss)
                },
                onError = { onIntent(EditMerchantProfileIntent.OnVenueImagePickerDismiss) },
                onDismiss = { onIntent(EditMerchantProfileIntent.OnVenueImagePickerDismiss) },
                allowMultiple = false
            )
        }
    }

    // Logo Picker
    Box {
        if (state.showLogoPicker) {
            GalleryPickerLauncher(
                onPhotosSelected = { photos ->
                    photos.firstOrNull()?.let { photo ->
                        coroutineScope.launch {
                            val bytes = photo.loadBytes()
                            if (bytes != null) {
                                onIntent(EditMerchantProfileIntent.OnLogoSelected(bytes))
                            } else {
                                onIntent(EditMerchantProfileIntent.OnLogoPickerDismiss)
                            }
                        }
                    } ?: onIntent(EditMerchantProfileIntent.OnLogoPickerDismiss)
                },
                onError = { onIntent(EditMerchantProfileIntent.OnLogoPickerDismiss) },
                onDismiss = { onIntent(EditMerchantProfileIntent.OnLogoPickerDismiss) },
                allowMultiple = false
            )
        }
    }

    // Lots Image Picker
    Box {
        if (state.showLotsImagePicker) {
            GalleryPickerLauncher(
                onPhotosSelected = { photos ->
                    photos.firstOrNull()?.let { photo ->
                        coroutineScope.launch {
                            val bytes = photo.loadBytes()
                            if (bytes != null) {
                                onIntent(EditMerchantProfileIntent.OnLotsImageSelected(bytes))
                            } else {
                                onIntent(EditMerchantProfileIntent.OnLotsImagePickerDismiss)
                            }
                        }
                    } ?: onIntent(EditMerchantProfileIntent.OnLotsImagePickerDismiss)
                },
                onError = { onIntent(EditMerchantProfileIntent.OnLotsImagePickerDismiss) },
                onDismiss = { onIntent(EditMerchantProfileIntent.OnLotsImagePickerDismiss) },
                allowMultiple = false
            )
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentPadding = PaddingValues(
            bottom = LessTheme.spacing.medium
        )
    ) {
        // Hero Section with overlapping logo and edit icons
        item(key = "hero_section") {
            EditMerchantProfileHeroSection(
                venueImageUrl = state.venueImageUrl,
                logoUrl = state.logoUrl,
                venueImageBytes = state.venueImageBytes,
                logoImageBytes = state.logoImageBytes,
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
                onNameEditClick = { onIntent(EditMerchantProfileIntent.OnEditMerchDetailsClick) },
                onDescriptionChanged = { onIntent(EditMerchantProfileIntent.OnEditMerchDetailsClick) }
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

        // Default Box Description and Add for Lots Sections
        item(key = "additional_sections") {
            EditBranchAdditionalSections(
                defaultBoxDescription = state.defaultBoxDescription,
                lotsImageUrl = state.lotsImageUrl,
                lotsImageBytes = state.lotsImageBytes,
                onDefaultBoxDescriptionChanged = { onIntent(EditMerchantProfileIntent.OnDefaultBoxDescriptionChanged(it)) },
                onLotsEditClick = { onIntent(EditMerchantProfileIntent.OnLotsEditClick) }
            )
        }

        // Toggle Section (using DsCell like in More screen)
        item(key = "toggle_section") {
            DsCell(
                title = stringResource(Res.string.edit_profile_make_me_merchant),
                type = CellType.Toggle(
                    checked = state.makeMeMerchant,
                    onCheckedChange = { onIntent(EditMerchantProfileIntent.OnMakeMeMerchantToggled(it)) }
                ),
                showDivider = false,
                modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
            )
        }

        // Create/Update Branch Button
        item(key = "submit_button") {
            DsButton(
                text = if (state.branchId == null) stringResource(Res.string.edit_profile_create_branch) else stringResource(Res.string.edit_profile_update_branch),
                onClick = { onIntent(EditMerchantProfileIntent.OnCreateBranchClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = LessTheme.spacing.medium,
                        vertical = LessTheme.spacing.medium
                    ),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large,
                isLoading = state.isLoading
            )
        }
    }

    // Edit Merch Details Bottom Sheet
    EditMerchDetailsBottomSheet(
        isVisible = state.isEditMerchDetailsBottomSheetVisible,
        sheetState = editMerchDetailsSheetState,
        initialTitle = state.venueName,
        initialDescription = state.description,
        onSave = { title, description ->
            onIntent(EditMerchantProfileIntent.OnEditMerchDetailsSave(title, description))
        },
        onDismiss = { onIntent(EditMerchantProfileIntent.OnEditMerchDetailsBottomSheetDismiss) }
    )

    // Edit Phone Number Bottom Sheet
    EditPhoneNumberBottomSheet(
        isVisible = state.isEditPhoneNumberBottomSheetVisible,
        sheetState = editPhoneNumberSheetState,
        initialPhoneNumber = state.phoneNumber,
        onSave = { phoneNumber ->
            onIntent(EditMerchantProfileIntent.OnEditPhoneNumberSave(phoneNumber))
        },
        onDismiss = { onIntent(EditMerchantProfileIntent.OnEditPhoneNumberBottomSheetDismiss) }
    )

}

