package az.less.mobile.presentation.merchant.places.edit.branchusers.addbranchuser

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.VenuesRepository
import az.less.mobile.presentation.merchant.places.edit.branchusers.BranchUser
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
    private val venuesRepository: VenuesRepository
) : ViewModel(), ContainerHost<AddBranchUserState, AddBranchUserSideEffect> {

    private val venueId: String = savedStateHandle.get<String>("venueId") ?: ""
    private val venueName: String = savedStateHandle.get<String>("venueName") ?: ""
    private val userNumber: Int = savedStateHandle.get<Int>("userNumber") ?: 1
    private val user: BranchUser? = savedStateHandle.get<String>("user")?.let {
        BranchUser.decode(it)
    }

    override val container: Container<AddBranchUserState, AddBranchUserSideEffect> =
        viewModelScope.container(
            AddBranchUserState(
                venueId = venueId,
                venueName = venueName,
                userId = user?.id,
                userNumber = userNumber,
                name = user?.name ?: "",
                phoneNumber = user?.phoneNumber ?: "",
                email = user?.email ?: ""
            )
        )

    fun onIntent(intent: AddBranchUserIntent) {
        when (intent) {
            is AddBranchUserIntent.OnBackClick -> onBackClick()
            is AddBranchUserIntent.OnNameChange -> onNameChange(intent.name)
            is AddBranchUserIntent.OnPhoneNumberChange -> onPhoneNumberChange(intent.phoneNumber)
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
        // Validate all fields
        val nameError = if (state.name.isBlank()) "Name is required" else null
        val phoneError = if (state.phoneNumber.length < 9) "Invalid phone number" else null
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

        val phone = if (state.phoneNumber.startsWith("+")) {
            state.phoneNumber
        } else {
            "+994${state.phoneNumber}"
        }

        venuesRepository.addVenueMerchant(
            venueId = state.venueId,
            userIdentifier = state.email,
            name = state.name,
            email = state.email,
            phone = phone
        )
            .onSuccess { response ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(
                    AddBranchUserSideEffect.UserSaved(
                        message = response.message ?: "User added successfully"
                    )
                )
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(AddBranchUserSideEffect.ShowError(error.message))
            }
    }

    private fun onDeleteClick() = intent {
        if (state.userId == null) return@intent

        reduce { state.copy(isLoading = true) }

        // TODO: Delete user via repository
        reduce { state.copy(isLoading = false) }
        postSideEffect(AddBranchUserSideEffect.UserDeleted)
    }
}
