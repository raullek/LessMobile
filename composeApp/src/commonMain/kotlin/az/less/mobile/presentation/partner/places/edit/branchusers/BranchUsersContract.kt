package az.less.mobile.presentation.partner.places.edit.branchusers

/**
 * User model for Branch Users screen
 */
data class BranchUser(
    /** Domain user id (used for self-detection: equals cached User.id). */
    val id: String,
    /** Venue-merchant relation id (`_id` in the API), used as the path param of
     *  the merchant-removal endpoint. */
    val merchantId: String,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val avatar: String? = null,
    val isActive: Boolean = true
) {
    /**
     * Encode user data to a simple delimited string for navigation
     */
    fun encode(): String = "$id|$merchantId|$name|$phoneNumber|$email"

    companion object {
        /**
         * Decode user data from delimited string
         */
        fun decode(encoded: String): BranchUser? {
            val parts = encoded.split("|")
            if (parts.size != 5) return null
            return BranchUser(
                id = parts[0],
                merchantId = parts[1],
                name = parts[2],
                phoneNumber = parts[3],
                email = parts[4]
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
