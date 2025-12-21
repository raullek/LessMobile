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
            is EditMerchantProfileIntent.OnDefaultBoxDescriptionChanged -> handleDefaultBoxDescriptionChanged(intent.description)
            is EditMerchantProfileIntent.OnManageFromEmailToggled -> handleManageFromEmailToggled(intent.enabled)
            is EditMerchantProfileIntent.OnCreateBranchClick -> handleCreateBranchClick()
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(EditMerchantProfileSideEffect.NavigateBack)
    }

    private fun handleVenueImageEditClick() = intent {
        postSideEffect(EditMerchantProfileSideEffect.NavigateToImagePicker)
    }

    private fun handleLogoEditClick() = intent {
        postSideEffect(EditMerchantProfileSideEffect.NavigateToLogoPicker)
    }

    private fun handleNameEditClick() = intent {
        // TODO: Open name edit dialog or navigate to edit screen
    }

    private fun handlePhoneEditClick() = intent {
        // TODO: Open phone edit dialog or navigate to edit screen
    }

    private fun handleLocationEditClick() = intent {
        // TODO: Open location picker or navigate to edit screen
    }

    private fun handleLotsEditClick() = intent {
        postSideEffect(EditMerchantProfileSideEffect.NavigateToLotsPicker)
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
}

