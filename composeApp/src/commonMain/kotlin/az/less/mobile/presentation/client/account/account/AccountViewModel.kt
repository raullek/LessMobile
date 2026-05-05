package az.less.mobile.presentation.client.account.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.designsystem.components.ToastType
import az.less.mobile.data.remote.model.account.UpdateUserRequest
import az.less.mobile.domain.model.auth.User
import az.less.mobile.domain.model.auth.UserVenue
import az.less.mobile.domain.repository.AccountRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.presentation.common.phone.PhoneCountry
import az.less.mobile.utils.displayDateToIsoDateTime
import az.less.mobile.utils.isoDateToDisplayDate
import kotlinx.coroutines.flow.first
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
        reduce { state.copy(isDataLoading = true) }
        val user = sessionLocalRepository.currentUser.firstOrNull()
        if (user != null) {
            val (country, local) = PhoneCountry.parse(user.phone.orEmpty())
            reduce {
                state.copy(
                    isDataLoading = false,
                    fullName = user.name,
                    email = user.email,
                    phoneNumber = local,
                    phoneCountry = country,
                    gender = Gender.fromApiValue(user.gender),
                    birthDate = user.birthDay?.isoDateToDisplayDate() ?: ""
                )
            }
        } else {
            reduce { state.copy(isDataLoading = false) }
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
            is AccountIntent.OnPhoneCountryChanged -> handlePhoneCountryChanged(intent.country)
            is AccountIntent.OnEmailChanged -> handleEmailChanged(intent.email)
            is AccountIntent.OnGenderChanged -> handleGenderChanged(intent.gender)
            is AccountIntent.OnBirthDateChanged -> handleBirthDateChanged(intent.birthDate)
            is AccountIntent.OnGenderClick -> handleGenderClick()
            is AccountIntent.OnGenderBottomSheetDismiss -> handleGenderBottomSheetDismiss()
            is AccountIntent.OnGenderClear -> handleGenderClear()
            is AccountIntent.OnBirthDateClick -> handleBirthDateClick()
            is AccountIntent.OnBirthDateClear -> handleBirthDateClear()
            is AccountIntent.OnDatePickerDismiss -> handleDatePickerDismiss()
            is AccountIntent.OnDatePickerConfirm -> handleDatePickerConfirm(intent.birthDate)
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
        reduce {
            state.copy(
                phoneNumber = phone,
                phoneNumberError = null
            )
        }
    }

    private fun handlePhoneCountryChanged(country: PhoneCountry) = intent {
        reduce {
            state.copy(
                phoneCountry = country,
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

    private fun validatePhoneNumber(phone: String, country: PhoneCountry): String? {
        if (phone.isEmpty()) return null
        return if (phone.length != country.localDigits) "Invalid phone number" else null
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
        reduce { state.copy(showDatePicker = true) }
    }

    private fun handleDatePickerDismiss() = intent {
        reduce { state.copy(showDatePicker = false) }
    }

    private fun handleDatePickerConfirm(birthDate: String) = intent {
        reduce { state.copy(showDatePicker = false, birthDate = birthDate) }
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
        val phoneError = validatePhoneNumber(state.phoneNumber, state.phoneCountry)
        reduce {
            state.copy(phoneNumberError = phoneError)
        }

        if (phoneError != null) return@intent

        reduce { state.copy(isLoading = true) }

        val phoneE164 = state.phoneNumber.takeIf { it.isNotEmpty() }
            ?.let { "+${state.phoneCountry.dialCode}$it" }

        val request = UpdateUserRequest(
            name = state.fullName.takeIf { it.isNotEmpty() },
            phone = phoneE164,
            gender = state.gender?.apiValue,
            birthDay = state.birthDate.displayDateToIsoDateTime()
        )

        accountRepository.updateUser(request)
            .onSuccess { data ->
                val currentUser = sessionLocalRepository.currentUser.first()
                val updatedUser = User(
                    id = data.id,
                    name = data.name,
                    email = data.email,
                    roles = data.roles,
                    status = data.status,
                    avatarUrl = data.avatar,
                    phone = data.phone,
                    gender = data.gender,
                    birthDay = data.birthDay,
                    emailVerified = data.emailVerified,
                    currentLocation = data.currentLocation,
                    venue = data.venue?.let {
                        UserVenue(
                            id = it.id,
                            name = it.name,
                            businessName = it.businessName,
                            businessAddress = it.businessAddress,
                            businessDescription = it.businessDescription,
                            businessLogo = it.businessLogo,
                            coverImage = it.coverImage,
                            rating = it.rating,
                            totalReviews = it.totalReviews,
                            status = it.status
                        )
                    },
                    stats = currentUser?.stats,
                    ecoHeroBadge = currentUser?.ecoHeroBadge
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
