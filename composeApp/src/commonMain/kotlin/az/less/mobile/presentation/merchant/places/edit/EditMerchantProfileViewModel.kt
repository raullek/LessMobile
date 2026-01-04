package az.less.mobile.presentation.merchant.places.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Edit Merchant Profile Screen using Orbit MVI
 */
class EditMerchantProfileViewModel : ViewModel(), ContainerHost<EditMerchantProfileState, EditMerchantProfileSideEffect> {

    override val container: Container<EditMerchantProfileState, EditMerchantProfileSideEffect> =
        viewModelScope.container(EditMerchantProfileState())

    fun initialize(branchId: String?) = intent {
        if (branchId != null) {
            reduce { state.copy(branchId = branchId) }
            loadBranch(branchId)
        }
    }

    private fun loadBranch(branchId: String) = intent {
        reduce { state.copy(isLoading = true) }

        // TODO: Load branch from repository
        // For now, using mock data
        reduce {
            state.copy(
                venueName = "Belgian Chocolate & Coffee",
                description = "Description or some notes about your perfect place to show fo customers",
                phoneNumber = "+994 12 123 45 67",
                location = "Location of your perfect place",
                defaultBoxDescription = "Some tips for user to understant what can be inside",
                manageFromEmail = false,
                isLoading = false
            )
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
            is EditMerchantProfileIntent.OnManageFromEmailToggled -> handleManageFromEmailToggled(intent.enabled)
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
        // TODO: Open name edit dialog or navigate to edit screen
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

    private fun handleManageFromEmailToggled(enabled: Boolean) = intent {
        reduce { state.copy(manageFromEmail = enabled) }
    }

    private fun handleCreateBranchClick() = intent {
        reduce { state.copy(isLoading = true) }

        // TODO: Save branch to repository
        // For now, simulate success
        reduce { state.copy(isLoading = false) }

        if (state.branchId == null) {
            postSideEffect(EditMerchantProfileSideEffect.BranchCreated)
        } else {
            postSideEffect(EditMerchantProfileSideEffect.BranchUpdated)
        }
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

