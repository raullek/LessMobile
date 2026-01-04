package az.less.mobile.presentation.client.main.saved

import az.less.mobile.presentation.client.main.saved.models.SavedMerchant

/**
 * State of the Saved/Favorites Screen
 */
data class SavedState(
    val savedMerchants: List<SavedMerchant> = emptyList(),
    val isLoading: Boolean = false
) {
    val isEmpty: Boolean get() = savedMerchants.isEmpty() && !isLoading
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface SavedSideEffect {
    data class ShowError(val message: String) : SavedSideEffect
    data class NavigateToMerchantDetail(val merchantId: String) : SavedSideEffect
    data object NavigateToExplore : SavedSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface SavedIntent {
    data class OnMerchantClicked(val merchantId: String) : SavedIntent
    data class OnRemoveFromFavorites(val merchantId: String) : SavedIntent
    data object OnExploreNewVenuesClicked : SavedIntent
}
