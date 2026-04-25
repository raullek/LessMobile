package az.less.mobile.presentation.partner.places.edit.branchusers

/**
 * User model for Branch Users screen
 */
data class BranchUser(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val avatar: String? = null,
    val isActive: Boolean = true
) {
    /**
     * Encode user data to a simple delimited string for navigation
     */
    fun encode(): String = "$id|$name|$phoneNumber|$email"

    companion object {
        /**
         * Decode user data from delimited string
         */
        fun decode(encoded: String): BranchUser? {
            val parts = encoded.split("|")
            if (parts.size != 4) return null
            return BranchUser(
                id = parts[0],
                name = parts[1],
                phoneNumber = parts[2],
                email = parts[3]
            )
        }
    }
}

/**
 * State of the Branch Users Screen
 */
data class BranchUsersState(
    val venueId: String = "",
    val branchName: String = "",
    val users: List<BranchUser> = emptyList(),
    val isLoading: Boolean = false
) {
    val isEmpty: Boolean get() = users.isEmpty() && !isLoading
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface BranchUsersSideEffect {
    data object NavigateBack : BranchUsersSideEffect
    data object NavigateToAddUser : BranchUsersSideEffect
    data class NavigateToEditUser(val userNumber: Int, val user: BranchUser) : BranchUsersSideEffect
    data class ShowError(val message: String) : BranchUsersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface BranchUsersIntent {
    data object OnBackClick : BranchUsersIntent
    data object OnAddUserClick : BranchUsersIntent
    data class OnUserClick(val userId: String) : BranchUsersIntent
    data class OnDeleteUserClick(val userId: String) : BranchUsersIntent
}
