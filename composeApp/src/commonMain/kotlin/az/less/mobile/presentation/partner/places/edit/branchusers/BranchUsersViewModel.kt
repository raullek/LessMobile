package az.less.mobile.presentation.partner.places.edit.branchusers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.remote.model.VenueMerchantDto
import az.less.mobile.domain.repository.VenuesRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Branch Users Screen using Orbit MVI
 */
class BranchUsersViewModel(
    savedStateHandle: SavedStateHandle,
    private val venuesRepository: VenuesRepository
) : ViewModel(), ContainerHost<BranchUsersState, BranchUsersSideEffect> {

    private val venueId: String = savedStateHandle["venueId"] ?: ""
    private val venueName: String = savedStateHandle["venueName"] ?: ""

    override val container: Container<BranchUsersState, BranchUsersSideEffect> =
        viewModelScope.container(
            BranchUsersState(
                venueId = venueId,
                branchName = venueName
            )
        )

    init {
        loadUsers()
    }

    private fun loadUsers() = intent {
        if (state.venueId.isEmpty()) return@intent

        reduce { state.copy(isLoading = true) }

        venuesRepository.getVenueMerchants(state.venueId)
            .onSuccess { merchants ->
                val users = merchants.map { it.toBranchUser() }
                reduce {
                    state.copy(
                        users = users,
                        isLoading = false
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(BranchUsersSideEffect.ShowError(error.message))
            }
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: BranchUsersIntent) {
        when (intent) {
            is BranchUsersIntent.OnBackClick -> handleBackClick()
            is BranchUsersIntent.OnAddUserClick -> handleAddUserClick()
            is BranchUsersIntent.OnUserClick -> handleUserClick(intent.userId)
            is BranchUsersIntent.OnDeleteUserClick -> handleDeleteUserClick(intent.userId)
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(BranchUsersSideEffect.NavigateBack)
    }

    private fun handleAddUserClick() = intent {
        postSideEffect(BranchUsersSideEffect.NavigateToAddUser)
    }

    private fun handleUserClick(userId: String) = intent {
        val userIndex = state.users.indexOfFirst { it.id == userId }
        val user = state.users.getOrNull(userIndex)
        if (user != null) {
            postSideEffect(
                BranchUsersSideEffect.NavigateToEditUser(
                    userNumber = userIndex + 1,
                    user = user
                )
            )
        }
    }

    private fun handleDeleteUserClick(userId: String) = intent {
        reduce {
            state.copy(users = state.users.filter { it.id != userId })
        }
    }
}

private fun VenueMerchantDto.toBranchUser(): BranchUser {
    return BranchUser(
        id = id ?: "",
        name = user?.name ?: "",
        phoneNumber = user?.phone ?: "",
        email = user?.email ?: "",
        avatar = user?.avatar,
        isActive = isActive ?: true
    )
}
