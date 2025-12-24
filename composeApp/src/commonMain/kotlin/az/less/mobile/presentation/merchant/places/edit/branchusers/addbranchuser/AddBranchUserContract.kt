package az.less.mobile.presentation.merchant.places.edit.branchusers.addbranchuser

/**
 * State of the Add Branch User Screen
 * @param userId null for create mode, non-null for edit mode
 */
data class AddBranchUserState(
    val userId: String? = null,
    val userNumber: Int = 1,
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val nameError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val isLoading: Boolean = false
) {
    val isEditMode: Boolean get() = userId != null
    
    val isFormValid: Boolean
        get() = name.isNotBlank() && 
                phoneNumber.length >= 9 && 
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
    data class ShowError(val message: String) : AddBranchUserSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface AddBranchUserIntent {
    data object OnBackClick : AddBranchUserIntent
    data class OnNameChange(val name: String) : AddBranchUserIntent
    data class OnPhoneNumberChange(val phoneNumber: String) : AddBranchUserIntent
    data class OnEmailChange(val email: String) : AddBranchUserIntent
    data object OnSaveClick : AddBranchUserIntent
    data object OnDeleteClick : AddBranchUserIntent
}

