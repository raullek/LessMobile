package az.less.mobile.presentation.merchant.places.edit.branchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Branch Users Screen using Orbit MVI
 */
class BranchUsersViewModel : ViewModel(), ContainerHost<BranchUsersState, BranchUsersSideEffect> {

    override val container: Container<BranchUsersState, BranchUsersSideEffect> =
        viewModelScope.container(BranchUsersState())

    init {
        loadUsers()
    }

    private fun loadUsers() = intent {
        reduce { state.copy(isLoading = true) }

        // TODO: Load users from repository
        // For now, using mock data matching Figma design
        val mockUsers = listOf(
            BranchUser(
                id = "1",
                name = "Mahammadali",
                phoneNumber = "501234567",
                email = "mahammadali@example.com"
            )
        )

        reduce {
            state.copy(
                users = mockUsers,
                isLoading = false
            )
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
        // Find user in list
        val userIndex = state.users.indexOfFirst { it.id == userId }
        val user = state.users.getOrNull(userIndex)
        if (user != null) {
            postSideEffect(BranchUsersSideEffect.NavigateToEditUser(
                userNumber = userIndex + 1,
                user = user
            ))
        }
    }

    private fun handleDeleteUserClick(userId: String) = intent {
        // TODO: Delete user from list
        reduce {
            state.copy(users = state.users.filter { it.id != userId })
        }
    }
}

