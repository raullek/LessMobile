package az.less.mobile.presentation.main.saved

import az.less.mobile.presentation.main.saved.models.FavoriteItem

/**
 * State of the Saved/Favorites Screen
 */
data class SavedState(
    val favoriteItems: List<FavoriteItem> = emptyList(),
    val isLoading: Boolean = false
) {
    val isEmpty: Boolean get() = favoriteItems.isEmpty() && !isLoading
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface SavedSideEffect {
    data class ShowError(val message: String) : SavedSideEffect
    data class NavigateToItemDetail(val itemId: String) : SavedSideEffect
    data object NavigateToExplore : SavedSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface SavedIntent {
    data class OnItemClicked(val itemId: String) : SavedIntent
    data class OnRemoveFromFavorites(val itemId: String) : SavedIntent
    data object OnExploreNewVenuesClicked : SavedIntent
}

