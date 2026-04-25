package az.less.mobile.presentation.partner.places.edit.selectlocation

/**
 * State for the Input Address Screen
 */
data class InputAddressState(
    val searchQuery: String = "",
    val searchResults: List<AddressSuggestion> = emptyList(),
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Address suggestion model
 */
data class AddressSuggestion(
    val id: String,
    val address: String,
    val subtitle: String? = null,
    val latitude: Double,
    val longitude: Double
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface InputAddressSideEffect {
    data object NavigateBack : InputAddressSideEffect
    data class AddressSelected(
        val address: String,
        val latitude: Double,
        val longitude: Double
    ) : InputAddressSideEffect
    data class ShowError(val message: String) : InputAddressSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface InputAddressIntent {
    data object OnBackClick : InputAddressIntent
    data class OnSearchQueryChanged(val query: String) : InputAddressIntent
    data class OnAddressSelected(val suggestion: AddressSuggestion) : InputAddressIntent
    data object OnClearSearch : InputAddressIntent
}
