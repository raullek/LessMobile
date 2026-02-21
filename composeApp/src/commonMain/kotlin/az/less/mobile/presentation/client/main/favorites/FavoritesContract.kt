package az.less.mobile.presentation.client.main.favorites

/**
 * State of the Favorites Screen
 */
data class FavoritesState(
    val isLoggedIn: Boolean = true
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface FavoritesSideEffect {
    data class ShowError(val message: String) : FavoritesSideEffect
    data class NavigateToMerchantDetail(val merchantId: String) : FavoritesSideEffect
    data object NavigateToExplore : FavoritesSideEffect
    data object NavigateToMore : FavoritesSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface FavoritesIntent {
    data class OnMerchantClicked(val merchantId: String) : FavoritesIntent
    data object OnExploreNewVenuesClicked : FavoritesIntent
    data object OnSignInClicked : FavoritesIntent
}
