package az.less.mobile.presentation.client.account.account

import az.less.designsystem.components.ToastType


data class AccountState(
    val isDataLoading: Boolean = true,
    val isLoading: Boolean = false,
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: Gender? = null,
    val birthDate: String = "",
    val showGenderBottomSheet: Boolean = false,
    val phoneNumberError: String? = null,
    val emailError: String? = null,
    val showProfilePhotoPicker: Boolean = false,
    val profilePhotoBytes: ByteArray? = null,
    val toastMessage: String? = null,
    val toastType: ToastType = ToastType.Success,
    val showDeleteConfirmation: Boolean = false,
    val showDatePicker: Boolean = false
)

sealed interface AccountSideEffect {
    data object NavigateBack : AccountSideEffect
    data object NavigateToLogin : AccountSideEffect
}

sealed interface AccountIntent {
    data object OnBackClicked : AccountIntent
    data object OnEditPhotoClicked : AccountIntent
    data class OnProfilePhotoSelected(val bytes: ByteArray) : AccountIntent
    data object OnProfilePhotoPickerDismiss : AccountIntent
    data class OnFullNameChanged(val fullName: String) : AccountIntent
    data class OnPhoneChanged(val phone: String) : AccountIntent
    data class OnEmailChanged(val email: String) : AccountIntent
    data class OnGenderChanged(val gender: Gender) : AccountIntent
    data class OnBirthDateChanged(val birthDate: String) : AccountIntent
    data object OnGenderClick : AccountIntent
    data object OnGenderBottomSheetDismiss : AccountIntent
    data object OnGenderClear : AccountIntent
    data object OnBirthDateClick : AccountIntent
    data object OnBirthDateClear : AccountIntent
    data object OnDatePickerDismiss : AccountIntent
    data class OnDatePickerConfirm(val birthDate: String) : AccountIntent
    data object OnDeleteAccountClicked : AccountIntent
    data object OnDeleteAccountConfirmed : AccountIntent
    data object OnDeleteAccountDismissed : AccountIntent
    data object OnSaveClicked : AccountIntent
    data object OnToastDismiss : AccountIntent
}
