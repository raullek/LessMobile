package az.less.mobile.presentation.partner.places.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.designsystem.components.ToastType
import az.less.mobile.domain.repository.VenuesRepository
import az.less.mobile.presentation.partner.places.model.BranchItem
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Edit Merchant Profile Screen using Orbit MVI
 */
class EditMerchantProfileViewModel(
    private val venuesRepository: VenuesRepository
) : ViewModel(), ContainerHost<EditMerchantProfileState, EditMerchantProfileSideEffect> {

    override val container: Container<EditMerchantProfileState, EditMerchantProfileSideEffect> =
        viewModelScope.container(EditMerchantProfileState())

    fun initialize(venueData: String?) = intent {
        if (venueData != null) {
            val branch = BranchItem.decode(venueData) ?: return@intent
            reduce {
                state.copy(
                    branchId = branch.id,
                    venueImageUrl = branch.imageUrl,
                    logoUrl = branch.logoUrl,
                    venueName = branch.name,
                    description = branch.businessDescription ?: "",
                    phoneNumber = branch.phone,
                    location = branch.address,
                    locationLatitude = branch.latitude,
                    locationLongitude = branch.longitude,
                    defaultBoxDescription = branch.defaultBoxDescription ?: "",
                    lotsImageUrl = branch.lotImageUrl,
                    email = branch.email,
                    rating = branch.rating
                )
            }
        }
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: EditMerchantProfileIntent) {
        when (intent) {
            is EditMerchantProfileIntent.OnBackClick -> handleBackClick()
            is EditMerchantProfileIntent.OnVenueImageEditClick -> handleVenueImageEditClick()
            is EditMerchantProfileIntent.OnLogoEditClick -> handleLogoEditClick()
            is EditMerchantProfileIntent.OnNameEditClick -> handleNameEditClick()
            is EditMerchantProfileIntent.OnPhoneEditClick -> handlePhoneEditClick()
            is EditMerchantProfileIntent.OnLocationEditClick -> handleLocationEditClick()
            is EditMerchantProfileIntent.OnLotsEditClick -> handleLotsEditClick()
            is EditMerchantProfileIntent.OnVenueNameChanged -> handleVenueNameChanged(intent.name)
            is EditMerchantProfileIntent.OnDescriptionChanged -> handleDescriptionChanged(intent.description)
            is EditMerchantProfileIntent.OnPhoneNumberChanged -> handlePhoneNumberChanged(intent.phone)
            is EditMerchantProfileIntent.OnLocationChanged -> handleLocationChanged(intent.location)
            is EditMerchantProfileIntent.OnLocationSelected -> handleLocationSelected(intent.latitude, intent.longitude, intent.address)
            is EditMerchantProfileIntent.OnDefaultBoxDescriptionChanged -> handleDefaultBoxDescriptionChanged(intent.description)
            is EditMerchantProfileIntent.OnMakeMeMerchantToggled -> handleMakeMeMerchantToggled(intent.enabled)
            is EditMerchantProfileIntent.OnCreateBranchClick -> handleCreateBranchClick()
            is EditMerchantProfileIntent.OnEditMerchDetailsClick -> handleEditMerchDetailsClick()
            is EditMerchantProfileIntent.OnEditMerchDetailsBottomSheetDismiss -> handleEditMerchDetailsBottomSheetDismiss()
            is EditMerchantProfileIntent.OnEditMerchDetailsSave -> handleEditMerchDetailsSave(intent.title, intent.description)
            is EditMerchantProfileIntent.OnEditPhoneNumberClick -> handleEditPhoneNumberClick()
            is EditMerchantProfileIntent.OnEditPhoneNumberBottomSheetDismiss -> handleEditPhoneNumberBottomSheetDismiss()
            is EditMerchantProfileIntent.OnEditPhoneNumberSave -> handleEditPhoneNumberSave(intent.phoneNumber)
            is EditMerchantProfileIntent.OnVenueImageSelected -> handleVenueImageSelected(intent.imageBytes)
            is EditMerchantProfileIntent.OnLogoSelected -> handleLogoSelected(intent.imageBytes)
            is EditMerchantProfileIntent.OnLotsImageSelected -> handleLotsImageSelected(intent.imageBytes)
            is EditMerchantProfileIntent.OnVenueImagePickerDismiss -> handleVenueImagePickerDismiss()
            is EditMerchantProfileIntent.OnLogoPickerDismiss -> handleLogoPickerDismiss()
            is EditMerchantProfileIntent.OnLotsImagePickerDismiss -> handleLotsImagePickerDismiss()
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(EditMerchantProfileSideEffect.NavigateBack)
    }

    private fun handleVenueImageEditClick() = intent {
        reduce { state.copy(showVenueImagePicker = true) }
    }

    private fun handleLogoEditClick() = intent {
        reduce { state.copy(showLogoPicker = true) }
    }

    private fun handleVenueImageSelected(imageBytes: ByteArray) = intent {
        reduce { state.copy(venueImageBytes = imageBytes, showVenueImagePicker = false) }
    }

    private fun handleLogoSelected(imageBytes: ByteArray) = intent {
        reduce { state.copy(logoImageBytes = imageBytes, showLogoPicker = false) }
    }

    private fun handleVenueImagePickerDismiss() = intent {
        reduce { state.copy(showVenueImagePicker = false) }
    }

    private fun handleLogoPickerDismiss() = intent {
        reduce { state.copy(showLogoPicker = false) }
    }

    private fun handleNameEditClick() = intent {
        // Open name edit via bottom sheet
    }

    private fun handlePhoneEditClick() = intent {
        reduce { state.copy(isEditPhoneNumberBottomSheetVisible = true) }
    }

    private fun handleLocationEditClick() = intent {
        postSideEffect(
            EditMerchantProfileSideEffect.NavigateToLocationPicker(
                latitude = state.locationLatitude,
                longitude = state.locationLongitude,
                address = state.location
            )
        )
    }

    private fun handleLocationSelected(latitude: Double, longitude: Double, address: String) = intent {
        reduce {
            state.copy(
                location = address,
                locationLatitude = latitude,
                locationLongitude = longitude
            )
        }
    }

    private fun handleLotsEditClick() = intent {
        reduce { state.copy(showLotsImagePicker = true) }
    }

    private fun handleLotsImageSelected(imageBytes: ByteArray) = intent {
        reduce { state.copy(lotsImageBytes = imageBytes, showLotsImagePicker = false) }
    }

    private fun handleLotsImagePickerDismiss() = intent {
        reduce { state.copy(showLotsImagePicker = false) }
    }

    private fun handleVenueNameChanged(name: String) = intent {
        reduce { state.copy(venueName = name) }
    }

    private fun handleDescriptionChanged(description: String) = intent {
        reduce { state.copy(description = description) }
    }

    private fun handlePhoneNumberChanged(phone: String) = intent {
        reduce { state.copy(phoneNumber = phone) }
    }

    private fun handleLocationChanged(location: String) = intent {
        reduce { state.copy(location = location) }
    }

    private fun handleDefaultBoxDescriptionChanged(description: String) = intent {
        reduce { state.copy(defaultBoxDescription = description) }
    }

    private fun handleMakeMeMerchantToggled(enabled: Boolean) = intent {
        reduce { state.copy(makeMeMerchant = enabled) }
    }

    private fun handleCreateBranchClick() = intent {
        // Validation
        if (state.venueName.isBlank()) {
            postSideEffect(EditMerchantProfileSideEffect.ShowToast(VALIDATION_NAME_REQUIRED, ToastType.Error))
            return@intent
        }
        if (state.phoneNumber.isBlank()) {
            postSideEffect(EditMerchantProfileSideEffect.ShowToast(VALIDATION_PHONE_REQUIRED, ToastType.Error))
            return@intent
        }
        if (state.location.isBlank()) {
            postSideEffect(EditMerchantProfileSideEffect.ShowToast(VALIDATION_LOCATION_REQUIRED, ToastType.Error))
            return@intent
        }

        reduce { state.copy(isLoading = true) }

        if (state.branchId == null) {
            // Create new venue
            venuesRepository.createVenue(
                name = state.venueName,
                businessName = state.venueName,
                businessAddress = state.location.ifEmpty { null },
                latitude = state.locationLatitude,
                longitude = state.locationLongitude,
                businessDescription = state.description.ifEmpty { null },
                defaultBoxDescription = state.defaultBoxDescription.ifEmpty { null },
                phone = state.phoneNumber.ifEmpty { null },
                email = state.email,
                makeMeMerchant = state.makeMeMerchant,
                coverImage = state.venueImageBytes,
                businessLogo = state.logoImageBytes,
                lotImage = state.lotsImageBytes
            )
                .onSuccess { venue ->
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(EditMerchantProfileSideEffect.ShowToast(SUCCESS_VENUE_CREATED, ToastType.Success))
                    postSideEffect(
                        EditMerchantProfileSideEffect.BranchCreated(
                            venueId = venue.id,
                            venueName = venue.name
                        )
                    )
                }
                .onError { error ->
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(EditMerchantProfileSideEffect.ShowToast(error.message, ToastType.Error))
                }
        } else {
            // Update existing venue — only send image bytes if user selected new ones
            venuesRepository.updateVenue(
                id = state.branchId!!,
                name = state.venueName,
                businessName = state.venueName,
                businessAddress = state.location.ifEmpty { null },
                latitude = state.locationLatitude,
                longitude = state.locationLongitude,
                businessDescription = state.description.ifEmpty { null },
                defaultBoxDescription = state.defaultBoxDescription.ifEmpty { null },
                phone = state.phoneNumber.ifEmpty { null },
                email = state.email,
                status = null,
                coverImage = state.venueImageBytes,
                businessLogo = state.logoImageBytes,
                lotImage = state.lotsImageBytes
            )
                .onSuccess {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(EditMerchantProfileSideEffect.ShowToast(SUCCESS_VENUE_UPDATED, ToastType.Success))
                    postSideEffect(EditMerchantProfileSideEffect.BranchUpdated)
                }
                .onError { error ->
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(EditMerchantProfileSideEffect.ShowToast(error.message, ToastType.Error))
                }
        }
    }

    companion object {
        const val VALIDATION_NAME_REQUIRED = "validation_name_required"
        const val VALIDATION_PHONE_REQUIRED = "validation_phone_required"
        const val VALIDATION_LOCATION_REQUIRED = "validation_location_required"
        const val SUCCESS_VENUE_CREATED = "success_venue_created"
        const val SUCCESS_VENUE_UPDATED = "success_venue_updated"
    }

    private fun handleEditMerchDetailsClick() = intent {
        reduce { state.copy(isEditMerchDetailsBottomSheetVisible = true) }
    }

    private fun handleEditMerchDetailsBottomSheetDismiss() = intent {
        reduce { state.copy(isEditMerchDetailsBottomSheetVisible = false) }
    }

    private fun handleEditMerchDetailsSave(title: String, description: String) = intent {
        reduce {
            state.copy(
                venueName = title,
                description = description,
                isEditMerchDetailsBottomSheetVisible = false
            )
        }
    }

    private fun handleEditPhoneNumberClick() = intent {
        reduce { state.copy(isEditPhoneNumberBottomSheetVisible = true) }
    }

    private fun handleEditPhoneNumberBottomSheetDismiss() = intent {
        reduce { state.copy(isEditPhoneNumberBottomSheetVisible = false) }
    }

    private fun handleEditPhoneNumberSave(phoneNumber: String) = intent {
        reduce {
            state.copy(
                phoneNumber = phoneNumber,
                isEditPhoneNumberBottomSheetVisible = false
            )
        }
    }
}
