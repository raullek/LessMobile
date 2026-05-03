package az.less.mobile.presentation.partner.places.edit.branchusers.addbranchuser

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.remote.model.mapper.toUser
import az.less.mobile.domain.model.auth.AppMode
import az.less.mobile.domain.repository.AccountRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.domain.repository.VenuesRepository
import az.less.mobile.presentation.common.phone.PhoneCountry
import az.less.mobile.presentation.partner.places.edit.branchusers.BranchUser
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Add Branch User Screen using Orbit MVI
 * Supports both create (user = null) and edit (user != null) modes
 * User data is passed via navigation as serialized JSON from BranchUsersScreen
 */
class AddBranchUserViewModel(
    savedStateHandle: SavedStateHandle,
    private val venuesRepository: VenuesRepository,
    private val accountRepository: AccountRepository,
    private val sessionLocalRepository: SessionLocalRepository
) : ViewModel(), ContainerHost<AddBranchUserState, AddBranchUserSideEffect> {

    private val venueId: String = savedStateHandle.get<String>("venueId") ?: ""
    private val venueName: String = savedStateHandle.get<String>("venueName") ?: ""
    private val userNumber: Int = savedStateHandle.get<Int>("userNumber") ?: 1
    private val user: BranchUser? = savedStateHandle.get<String>("user")?.let {
        BranchUser.decode(it)
    }

    override val container: Container<AddBranchUserState, AddBranchUserSideEffect> =
        viewModelScope.container(
            run {
                val (country, local) = PhoneCountry.parse(user?.phoneNumber.orEmpty())
                AddBranchUserState(
                    venueId = venueId,
                    venueName = venueName,
                    userId = user?.id,
                    merchantId = user?.merchantId,
                    userNumber = userNumber,
                    name = user?.name ?: "",
                    phoneNumber = local,
                    phoneCountry = country,
                    email = user?.email ?: ""
                )
            }
        ) {
            // Detect whether the user being edited is the cached current user AND
            // whether they are currently a merchant of THIS venue. Only then does
            // a self-delete trigger the role-replace flow.
            val cached = sessionLocalRepository.currentUser.first()
            val isSelfMerchant = cached != null
                    && user?.id != null
                    && cached.id == user.id
                    && cached.isMerchant
                    && cached.venue?.id == venueId
            reduce { state.copy(isSelfMerchant = isSelfMerchant) }
        }

    fun onIntent(intent: AddBranchUserIntent) {
        when (intent) {
            is AddBranchUserIntent.OnBackClick -> onBackClick()
            is AddBranchUserIntent.OnNameChange -> onNameChange(intent.name)
            is AddBranchUserIntent.OnPhoneNumberChange -> onPhoneNumberChange(intent.phoneNumber)
            is AddBranchUserIntent.OnPhoneCountryChange -> onPhoneCountryChange(intent.country)
            is AddBranchUserIntent.OnEmailChange -> onEmailChange(intent.email)
            is AddBranchUserIntent.OnSaveClick -> onSaveClick()
            is AddBranchUserIntent.OnDeleteClick -> onDeleteClick()
        }
    }

    private fun onBackClick() = intent {
        postSideEffect(AddBranchUserSideEffect.NavigateBack)
    }

    private fun onNameChange(name: String) = intent {
        reduce {
            state.copy(
                name = name,
                nameError = null
            )
        }
    }

    private fun onPhoneNumberChange(phoneNumber: String) = intent {
        reduce {
            state.copy(
                phoneNumber = phoneNumber,
                phoneError = null
            )
        }
    }

    private fun onPhoneCountryChange(country: PhoneCountry) = intent {
        reduce {
            state.copy(
                phoneCountry = country,
                phoneError = null
            )
        }
    }

    private fun onEmailChange(email: String) = intent {
        reduce {
            state.copy(
                email = email,
                emailError = validateEmail(email)
            )
        }
    }

    private fun validateEmail(email: String): String? {
        if (email.isBlank()) return null
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return if (!emailRegex.matches(email)) "Invalid email format" else null
    }

    private fun onSaveClick() = intent {
        val nameError = if (state.name.isBlank()) "Name is required" else null
        val phoneError = if (state.phoneNumber.length != state.phoneCountry.localDigits) "Invalid phone number" else null
        val emailError = validateEmail(state.email) ?: if (state.email.isBlank()) "Email is required" else null

        if (nameError != null || phoneError != null || emailError != null) {
            reduce {
                state.copy(
                    nameError = nameError,
                    phoneError = phoneError,
                    emailError = emailError
                )
            }
            return@intent
        }

        reduce { state.copy(isLoading = true) }

        val phone = "+${state.phoneCountry.dialCode}${state.phoneNumber}"

        venuesRepository.addVenueMerchant(
            venueId = state.venueId,
            userIdentifier = state.email,
            name = state.name,
            email = state.email,
            phone = phone
        )
            .onSuccess {
                // The added email may belong to the cached current user (partner adds
                // themselves as a merchant of this venue). Refetch the profile and,
                // if the merchant role just appeared, switch the nav root.
                val before = sessionLocalRepository.currentUser.first()
                val wasMerchantBefore = before?.isMerchant == true
                var becameMerchant = false
                accountRepository.getProfile()
                    .onSuccess { profile ->
                        val refreshed = profile.toUser()
                        sessionLocalRepository.updateUser(refreshed)
                        becameMerchant = !wasMerchantBefore && refreshed.isMerchant
                    }
                if (becameMerchant) {
                    sessionLocalRepository.saveLastUsedMode(AppMode.MERCHANT)
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(AddBranchUserSideEffect.UserSavedSwitchToMerchant)
                } else {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(AddBranchUserSideEffect.UserSaved)
                }
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(AddBranchUserSideEffect.ShowError(error.message))
            }
    }

    private fun onDeleteClick() = intent {
        val merchantId = state.merchantId ?: return@intent

        reduce { state.copy(isLoading = true) }

        venuesRepository.removeVenueMerchant(merchantId)
            .onSuccess {
                if (state.isSelfMerchant) {
                    // Server may have auto-stripped the merchant role. Refetch the
                    // profile to find out, persist locally, then ask the screen to
                    // swap the navigation root back to partner.
                    accountRepository.getProfile()
                        .onSuccess { profile ->
                            sessionLocalRepository.updateUser(profile.toUser())
                        }
                    sessionLocalRepository.saveLastUsedMode(AppMode.PARTNER)
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(AddBranchUserSideEffect.SelfDeletedSwitchToPartner)
                } else {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(AddBranchUserSideEffect.UserDeleted)
                }
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(AddBranchUserSideEffect.ShowError(error.message))
            }
    }
}
