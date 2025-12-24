package az.less.mobile.presentation.merchant.places.edit

/**
 * State of the Edit Merchant Profile Screen
 */
data class EditMerchantProfileState(
    val branchId: String? = null, // null for new branch, non-null for editing
    val venueImageUrl: String? = null,
    val logoUrl: String? = null,
    val venueName: String = "",
    val description: String = "",
    val phoneNumber: String = "",
    val location: String = "",
    val defaultBoxDescription: String = "",
    val lotsImageUrl: String? = null,
    val manageFromEmail: Boolean = false,
    val isLoading: Boolean = false,
    // Edit Merch Details Bottom Sheet state
    val isEditMerchDetailsBottomSheetVisible: Boolean = false,
    // Edit Phone Number Bottom Sheet state
    val isEditPhoneNumberBottomSheetVisible: Boolean = false
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface EditMerchantProfileSideEffect {
    data object NavigateBack : EditMerchantProfileSideEffect
    data object NavigateToImagePicker : EditMerchantProfileSideEffect
    data object NavigateToLogoPicker : EditMerchantProfileSideEffect
    data object NavigateToLotsPicker : EditMerchantProfileSideEffect
    data class ShowError(val message: String) : EditMerchantProfileSideEffect
    data object BranchCreated : EditMerchantProfileSideEffect
    data object BranchUpdated : EditMerchantProfileSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface EditMerchantProfileIntent {
    data object OnBackClick : EditMerchantProfileIntent
    data object OnVenueImageEditClick : EditMerchantProfileIntent
    data object OnLogoEditClick : EditMerchantProfileIntent
    data object OnNameEditClick : EditMerchantProfileIntent
    data object OnPhoneEditClick : EditMerchantProfileIntent
    data object OnLocationEditClick : EditMerchantProfileIntent
    data object OnLotsEditClick : EditMerchantProfileIntent
    data class OnVenueNameChanged(val name: String) : EditMerchantProfileIntent
    data class OnDescriptionChanged(val description: String) : EditMerchantProfileIntent
    data class OnPhoneNumberChanged(val phone: String) : EditMerchantProfileIntent
    data class OnLocationChanged(val location: String) : EditMerchantProfileIntent
    data class OnDefaultBoxDescriptionChanged(val description: String) : EditMerchantProfileIntent
    data class OnManageFromEmailToggled(val enabled: Boolean) : EditMerchantProfileIntent
    data object OnCreateBranchClick : EditMerchantProfileIntent
    // Edit Merch Details Bottom Sheet
    data object OnEditMerchDetailsClick : EditMerchantProfileIntent
    data object OnEditMerchDetailsBottomSheetDismiss : EditMerchantProfileIntent
    data class OnEditMerchDetailsSave(val title: String, val description: String) : EditMerchantProfileIntent
    // Edit Phone Number Bottom Sheet
    data object OnEditPhoneNumberClick : EditMerchantProfileIntent
    data object OnEditPhoneNumberBottomSheetDismiss : EditMerchantProfileIntent
    data class OnEditPhoneNumberSave(val phoneNumber: String) : EditMerchantProfileIntent
}

