package az.less.mobile.presentation.client.account.account


data class AccountState(
    val isLoading: Boolean = false,
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: String = "",
    val birthDate: String = "",
    val showGenderBottomSheet: Boolean = false,
    val genderList: List<String> = listOf("Male", "Female", "Don't want to specify"),
    val phoneNumberError: String? = null,
    val emailError: String? = null,
    val showProfilePhotoPicker: Boolean = false,
    val profilePhotoBytes: ByteArray? = null
)

interface AccountSideEffect {
    data object NavigateBack : AccountSideEffect
    data class ShowError(val message: String) : AccountSideEffect
}

sealed interface AccountIntent {
    data object OnBackClicked : AccountIntent
    data object OnEditPhotoClicked : AccountIntent
    data class OnProfilePhotoSelected(val bytes: ByteArray) : AccountIntent
    data object OnProfilePhotoPickerDismiss : AccountIntent
    data class OnFullNameChanged(val fullName: String) : AccountIntent
    data class OnPhoneChanged(val phone: String) : AccountIntent
    data class OnEmailChanged(val email: String) : AccountIntent
    data class OnGenderChanged(val gender: String) : AccountIntent
    data class OnBirthDateChanged(val birthDate: String) : AccountIntent
    data object OnGenderClick : AccountIntent
    data object OnGenderBottomSheetDismiss : AccountIntent
    data object OnGenderClear : AccountIntent
    data object OnBirthDateClick : AccountIntent
    data object OnBirthDateClear : AccountIntent
    data object OnDeleteAccountClicked : AccountIntent
    data object OnSaveClicked : AccountIntent
}
