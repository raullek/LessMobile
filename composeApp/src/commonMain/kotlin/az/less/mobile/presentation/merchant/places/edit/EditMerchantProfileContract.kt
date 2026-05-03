package az.less.mobile.presentation.merchant.places.edit

import az.less.designsystem.components.ToastType

/**
 * State of the Edit Merchant Profile Screen
 */
data class EditMerchantProfileState(
    val branchId: String? = null, // null for new branch, non-null for editing
    val venueImageUrl: String? = null,
    val logoUrl: String? = null,
    val venueImageBytes: ByteArray? = null, // Selected venue image bytes
    val logoImageBytes: ByteArray? = null, // Selected logo image bytes
    val venueName: String = "",
    val description: String = "",
    val phoneNumber: String = "",
    val location: String = "",
    val locationLatitude: Double? = null,
    val locationLongitude: Double? = null,
    val defaultBoxDescription: String = "",
    val lotsImageUrl: String? = null,
    val lotsImageBytes: ByteArray? = null, // Selected lots image bytes
    val email: String? = null,
    val rating: Float = 0f,
    val makeMeMerchant: Boolean = false,
    /** True if the cached current user already has an attached venue (`User.venue != null`).
     *  Used to hide the "make me merchant for this venue" toggle. */
    val hasAttachedVenue: Boolean = false,
    val isLoading: Boolean = false,
    // Edit Merch Details Bottom Sheet state
    val isEditMerchDetailsBottomSheetVisible: Boolean = false,
    // Edit Phone Number Bottom Sheet state
    val isEditPhoneNumberBottomSheetVisible: Boolean = false,
    // Image Picker state
    val showVenueImagePicker: Boolean = false,
    val showLogoPicker: Boolean = false,
    val showLotsImagePicker: Boolean = false

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as EditMerchantProfileState

        if (branchId != other.branchId) return false
        if (venueImageUrl != other.venueImageUrl) return false
        if (logoUrl != other.logoUrl) return false
        if (venueImageBytes != null) {
            if (other.venueImageBytes == null) return false
            if (!venueImageBytes.contentEquals(other.venueImageBytes)) return false
        } else if (other.venueImageBytes != null) return false
        if (logoImageBytes != null) {
            if (other.logoImageBytes == null) return false
            if (!logoImageBytes.contentEquals(other.logoImageBytes)) return false
        } else if (other.logoImageBytes != null) return false
        if (venueName != other.venueName) return false
        if (description != other.description) return false
        if (phoneNumber != other.phoneNumber) return false
        if (location != other.location) return false
        if (locationLatitude != other.locationLatitude) return false
        if (locationLongitude != other.locationLongitude) return false
        if (defaultBoxDescription != other.defaultBoxDescription) return false
        if (lotsImageUrl != other.lotsImageUrl) return false
        if (lotsImageBytes != null) {
            if (other.lotsImageBytes == null) return false
            if (!lotsImageBytes.contentEquals(other.lotsImageBytes)) return false
        } else if (other.lotsImageBytes != null) return false
        if (email != other.email) return false
        if (rating != other.rating) return false
        if (makeMeMerchant != other.makeMeMerchant) return false
        if (hasAttachedVenue != other.hasAttachedVenue) return false
        if (isLoading != other.isLoading) return false
        if (isEditMerchDetailsBottomSheetVisible != other.isEditMerchDetailsBottomSheetVisible) return false
        if (isEditPhoneNumberBottomSheetVisible != other.isEditPhoneNumberBottomSheetVisible) return false
        if (showVenueImagePicker != other.showVenueImagePicker) return false
        if (showLogoPicker != other.showLogoPicker) return false
        if (showLotsImagePicker != other.showLotsImagePicker) return false

        return true
    }

    override fun hashCode(): Int {
        var result = branchId?.hashCode() ?: 0
        result = 31 * result + (venueImageUrl?.hashCode() ?: 0)
        result = 31 * result + (logoUrl?.hashCode() ?: 0)
        result = 31 * result + (venueImageBytes?.contentHashCode() ?: 0)
        result = 31 * result + (logoImageBytes?.contentHashCode() ?: 0)
        result = 31 * result + venueName.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + (locationLatitude?.hashCode() ?: 0)
        result = 31 * result + (locationLongitude?.hashCode() ?: 0)
        result = 31 * result + defaultBoxDescription.hashCode()
        result = 31 * result + (lotsImageUrl?.hashCode() ?: 0)
        result = 31 * result + (lotsImageBytes?.contentHashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + rating.hashCode()
        result = 31 * result + makeMeMerchant.hashCode()
        result = 31 * result + hasAttachedVenue.hashCode()
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + isEditMerchDetailsBottomSheetVisible.hashCode()
        result = 31 * result + isEditPhoneNumberBottomSheetVisible.hashCode()
        result = 31 * result + showVenueImagePicker.hashCode()
        result = 31 * result + showLogoPicker.hashCode()
        result = 31 * result + showLotsImagePicker.hashCode()
        return result
    }
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface EditMerchantProfileSideEffect {
    data object NavigateBack : EditMerchantProfileSideEffect
    data object NavigateToImagePicker : EditMerchantProfileSideEffect
    data object NavigateToLogoPicker : EditMerchantProfileSideEffect
    data object NavigateToLotsPicker : EditMerchantProfileSideEffect
    data class NavigateToLocationPicker(val latitude: Double?, val longitude: Double?, val address: String?) : EditMerchantProfileSideEffect
    data object BranchCreated : EditMerchantProfileSideEffect
    data object BranchUpdated : EditMerchantProfileSideEffect
    data class ShowToast(val message: String, val type: ToastType) : EditMerchantProfileSideEffect
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
    data class OnLocationSelected(val latitude: Double, val longitude: Double, val address: String) : EditMerchantProfileIntent
    data class OnDefaultBoxDescriptionChanged(val description: String) : EditMerchantProfileIntent
    data class OnMakeMeMerchantToggled(val enabled: Boolean) : EditMerchantProfileIntent
    data object OnCreateBranchClick : EditMerchantProfileIntent
    // Edit Merch Details Bottom Sheet
    data object OnEditMerchDetailsClick : EditMerchantProfileIntent
    data object OnEditMerchDetailsBottomSheetDismiss : EditMerchantProfileIntent
    data class OnEditMerchDetailsSave(val title: String, val description: String) : EditMerchantProfileIntent
    // Edit Phone Number Bottom Sheet
    data object OnEditPhoneNumberClick : EditMerchantProfileIntent
    data object OnEditPhoneNumberBottomSheetDismiss : EditMerchantProfileIntent
    data class OnEditPhoneNumberSave(val phoneNumber: String) : EditMerchantProfileIntent
    // Image Picker
    data class OnVenueImageSelected(val imageBytes: ByteArray) : EditMerchantProfileIntent
    data class OnLogoSelected(val imageBytes: ByteArray) : EditMerchantProfileIntent
    data class OnLotsImageSelected(val imageBytes: ByteArray) : EditMerchantProfileIntent
    data object OnVenueImagePickerDismiss : EditMerchantProfileIntent
    data object OnLogoPickerDismiss : EditMerchantProfileIntent
    data object OnLotsImagePickerDismiss : EditMerchantProfileIntent
}

