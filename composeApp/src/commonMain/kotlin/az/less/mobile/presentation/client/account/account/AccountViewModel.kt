package az.less.mobile.presentation.client.account.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.designsystem.components.ToastType
import az.less.mobile.data.remote.model.account.UpdateUserRequest
import az.less.mobile.domain.model.auth.User
import az.less.mobile.domain.repository.AccountRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import kotlinx.coroutines.flow.firstOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class AccountViewModel(
    private val sessionLocalRepository: SessionLocalRepository,
    private val accountRepository: AccountRepository
) : ViewModel(), ContainerHost<AccountState, AccountSideEffect> {

    override val container: Container<AccountState, AccountSideEffect> =
        viewModelScope.container(AccountState())

    init {
        loadUserData()
    }

    private fun loadUserData() = intent {
        reduce { state.copy(isLoading = true) }
        val user = sessionLocalRepository.currentUser.firstOrNull()
        if (user != null) {
            reduce {
                state.copy(
                    isLoading = false,
                    userId = user.id,
                    fullName = user.name,
                    email = user.email,
                    phoneNumber = user.phone?.removePrefix("+994") ?: "",
                    gender = Gender.fromApiValue(user.gender),
                    birthDate = user.birthDay?.take(10)?.isoDateToDisplay() ?: ""
                )
            }
        } else {
            reduce { state.copy(isLoading = false) }
        }
    }

    fun onIntent(intent: AccountIntent) {
        when (intent) {
            is AccountIntent.OnBackClicked -> handleBackClicked()
            is AccountIntent.OnEditPhotoClicked -> handleEditPhotoClicked()
            is AccountIntent.OnProfilePhotoSelected -> handleProfilePhotoSelected(intent.bytes)
            is AccountIntent.OnProfilePhotoPickerDismiss -> handleProfilePhotoPickerDismiss()
            is AccountIntent.OnFullNameChanged -> handleFullNameChanged(intent.fullName)
            is AccountIntent.OnPhoneChanged -> handlePhoneChanged(intent.phone)
            is AccountIntent.OnEmailChanged -> handleEmailChanged(intent.email)
            is AccountIntent.OnGenderChanged -> handleGenderChanged(intent.gender)
            is AccountIntent.OnBirthDateChanged -> handleBirthDateChanged(intent.birthDate)
            is AccountIntent.OnGenderClick -> handleGenderClick()
            is AccountIntent.OnGenderBottomSheetDismiss -> handleGenderBottomSheetDismiss()
            is AccountIntent.OnGenderClear -> handleGenderClear()
            is AccountIntent.OnBirthDateClick -> handleBirthDateClick()
            is AccountIntent.OnBirthDateClear -> handleBirthDateClear()
            is AccountIntent.OnDeleteAccountClicked -> handleDeleteAccountClicked()
            is AccountIntent.OnDeleteAccountConfirmed -> handleDeleteAccountConfirmed()
            is AccountIntent.OnDeleteAccountDismissed -> handleDeleteAccountDismissed()
            is AccountIntent.OnSaveClicked -> handleSaveClicked()
            is AccountIntent.OnToastDismiss -> handleToastDismiss()
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(AccountSideEffect.NavigateBack)
    }

    private fun handleEditPhotoClicked() = intent {
        reduce {
            state.copy(showProfilePhotoPicker = true)
        }
    }

    private fun handleProfilePhotoSelected(bytes: ByteArray) = intent {
        reduce {
            state.copy(
                profilePhotoBytes = bytes,
                showProfilePhotoPicker = false
            )
        }
    }

    private fun handleProfilePhotoPickerDismiss() = intent {
        reduce {
            state.copy(showProfilePhotoPicker = false)
        }
    }

    private fun handleFullNameChanged(fullName: String) = intent {
        reduce {
            state.copy(fullName = fullName)
        }
    }

    private fun handlePhoneChanged(phone: String) = intent {
        val cleanedPhone = if (phone.startsWith("+994")) {
            phone.substring(4)
        } else {
            phone
        }
        reduce {
            state.copy(
                phoneNumber = cleanedPhone,
                phoneNumberError = null
            )
        }
    }

    private fun handleEmailChanged(email: String) = intent {
        reduce {
            state.copy(
                email = email,
                emailError = null
            )
        }
    }

    private fun validatePhoneNumber(phone: String): String? {
        if (phone.isEmpty()) return null

        val cleanedPhone = phone.replace(" ", "").replace("-", "").replace("(", "").replace(")", "")

        return when {
            !cleanedPhone.startsWith("+994") -> "Phone number must start with +994"
            cleanedPhone.length < 13 -> "Phone number is too short"
            cleanedPhone.length > 13 -> "Phone number is too long"
            !cleanedPhone.substring(4).all { it.isDigit() } -> "Phone number contains invalid characters"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        if (email.isEmpty()) return null

        return when {
            !email.contains("@") -> "Email must contain @"
            else -> null
        }
    }

    private fun handleGenderChanged(gender: Gender) = intent {
        reduce {
            state.copy(
                gender = gender,
                showGenderBottomSheet = false
            )
        }
    }

    private fun handleBirthDateChanged(birthDate: String) = intent {
        reduce {
            state.copy(birthDate = birthDate)
        }
    }

    private fun handleGenderClick() = intent {
        reduce {
            state.copy(showGenderBottomSheet = true)
        }
    }

    private fun handleGenderBottomSheetDismiss() = intent {
        reduce {
            state.copy(showGenderBottomSheet = false)
        }
    }

    private fun handleGenderClear() = intent {
        reduce {
            state.copy(gender = null)
        }
    }

    private fun handleBirthDateClick() = intent {
    }

    private fun handleBirthDateClear() = intent {
        reduce {
            state.copy(birthDate = "")
        }
    }

    private fun handleToastDismiss() = intent {
        reduce { state.copy(toastMessage = null) }
    }

    private fun handleDeleteAccountClicked() = intent {
        reduce { state.copy(showDeleteConfirmation = true) }
    }

    private fun handleDeleteAccountDismissed() = intent {
        reduce { state.copy(showDeleteConfirmation = false) }
    }

    private fun handleDeleteAccountConfirmed() = intent {
        reduce { state.copy(showDeleteConfirmation = false, isLoading = true) }

        accountRepository.deleteProfile()
            .onSuccess {
                sessionLocalRepository.clearSession()
                reduce { state.copy(isLoading = false) }
                postSideEffect(AccountSideEffect.NavigateToLogin)
            }
            .onError { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        toastMessage = error.message,
                        toastType = ToastType.Error
                    )
                }
            }
    }

    private fun handleSaveClicked() = intent {
        val phoneForValidation = if (state.phoneNumber.isNotEmpty() && !state.phoneNumber.startsWith("+994")) {
            "+994${state.phoneNumber}"
        } else {
            state.phoneNumber
        }
        val phoneError = validatePhoneNumber(phoneForValidation)
        reduce {
            state.copy(phoneNumberError = phoneError)
        }

        if (phoneError != null) return@intent

        val userId = state.userId ?: return@intent

        reduce { state.copy(isLoading = true) }

        val request = UpdateUserRequest(
            name = state.fullName.takeIf { it.isNotEmpty() },
            phone = phoneForValidation.takeIf { it.isNotEmpty() },
            gender = state.gender?.apiValue,
            birthDay = state.birthDate.displayDateToIso()
        )

        accountRepository.updateUser(userId, request)
            .onSuccess { data ->
                val updatedUser = User(
                    id = data.id,
                    name = data.name,
                    email = data.email,
                    roles = data.roles,
                    status = data.status,
                    avatarUrl = data.avatar,
                    phone = data.phone,
                    gender = data.gender,
                    birthDay = data.birthDay
                )
                sessionLocalRepository.updateUser(updatedUser)
                reduce {
                    state.copy(
                        isLoading = false,
                        toastMessage = "Profile updated",
                        toastType = ToastType.Success
                    )
                }
            }
            .onError { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        toastMessage = error.message,
                        toastType = ToastType.Error
                    )
                }
            }
    }
}
