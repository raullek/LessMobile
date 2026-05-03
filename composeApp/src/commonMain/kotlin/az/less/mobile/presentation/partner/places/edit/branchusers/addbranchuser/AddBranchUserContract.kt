package az.less.mobile.presentation.partner.places.edit.branchusers.addbranchuser

import az.less.mobile.presentation.common.phone.PhoneCountry

/**
 * State of the Add Branch User Screen
 * @param userId null for create mode, non-null for edit mode
 */
data class AddBranchUserState(
    val venueId: String = "",
    val venueName: String = "",
    val userId: String? = null,
    /** Venue-merchant relation id, required for the delete endpoint. */
    val merchantId: String? = null,
    val userNumber: Int = 1,
    val name: String = "",
    val phoneNumber: String = "",
    val phoneCountry: PhoneCountry = PhoneCountry.Default,
    val email: String = "",
    val nameError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val isLoading: Boolean = false,
    /** True when the current cached user is the same as [userId] AND is currently a
     *  merchant of this venue. When true, deleting this user from venue merchants
     *  triggers a profile refetch and a navigation-root switch back to partner. */
    val isSelfMerchant: Boolean = false
) {
    val isExistingUser: Boolean get() = userId != null

    val isFormValid: Boolean
        get() = name.isNotBlank() &&
                phoneNumber.length == phoneCountry.localDigits &&
                email.isValidEmail() &&
                nameError == null &&
                phoneError == null &&
                emailError == null
}

/**
 * Check if email is valid
 */
private fun String.isValidEmail(): Boolean {
    if (isBlank()) return false
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return emailRegex.matches(this)
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface AddBranchUserSideEffect {
    data object NavigateBack : AddBranchUserSideEffect
    data object UserSaved : AddBranchUserSideEffect
    data object UserDeleted : AddBranchUserSideEffect
    /** Self-deleted from a venue and the user is no longer a merchant — caller
     *  should switch the navigation root from MERCHANT back to PARTNER. */
    data object SelfDeletedSwitchToPartner : AddBranchUserSideEffect
    /** Self-added as a merchant of this venue — the profile refetch confirmed
     *  the merchant role is now present. Caller should switch the navigation
     *  root from PARTNER to MERCHANT. */
    data object UserSavedSwitchToMerchant : AddBranchUserSideEffect
    data class ShowError(val message: String) : AddBranchUserSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface AddBranchUserIntent {
    data object OnBackClick : AddBranchUserIntent
    data class OnNameChange(val name: String) : AddBranchUserIntent
    data class OnPhoneNumberChange(val phoneNumber: String) : AddBranchUserIntent
    data class OnPhoneCountryChange(val country: PhoneCountry) : AddBranchUserIntent
    data class OnEmailChange(val email: String) : AddBranchUserIntent
    data object OnSaveClick : AddBranchUserIntent
    data object OnDeleteClick : AddBranchUserIntent
}

