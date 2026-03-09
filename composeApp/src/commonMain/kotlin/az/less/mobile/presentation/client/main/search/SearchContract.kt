package az.less.mobile.presentation.client.main.search

import az.less.mobile.presentation.client.main.search.models.SearchCategory

/**
 * State of the Search Screen
 */
data class SearchState(
    val searchQuery: String = "",
    val categories: List<SearchCategory> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface SearchSideEffect {
    data class ShowError(val message: String) : SearchSideEffect
    data class NavigateToCategoryOffers(
        val categoryId: String,
        val categoryType: String,
        val categoryTitle: String
    ) : SearchSideEffect
    data object NavigateBack : SearchSideEffect
    data object NavigateToMap : SearchSideEffect
    data class NavigateToMerchantProfile(val merchantId: String) : SearchSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface SearchIntent {
    data class OnSearchQueryChanged(val query: String) : SearchIntent
    data class OnCategorySelected(val categoryId: String) : SearchIntent
    data class OnVenueClicked(val venueId: String) : SearchIntent
    data object OnBackClicked : SearchIntent
    data object OnMapClicked : SearchIntent
}
