package az.less.mobile.presentation.client.main.search

import az.less.mobile.presentation.client.main.search.models.SearchCategory
import az.less.mobile.presentation.client.main.search.models.SearchOffer

/**
 * State of the Search Screen
 */
data class SearchState(
    val searchQuery: String = "",
    val categories: List<az.less.mobile.presentation.client.main.search.models.SearchCategory> = emptyList(),
    val offers: List<az.less.mobile.presentation.client.main.search.models.SearchOffer> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface SearchSideEffect {
    data class ShowError(val message: String) :
        az.less.mobile.presentation.client.main.search.SearchSideEffect
    data class NavigateToCategory(val categoryId: String) :
        az.less.mobile.presentation.client.main.search.SearchSideEffect
    data object NavigateBack : az.less.mobile.presentation.client.main.search.SearchSideEffect
    data object NavigateToMap : az.less.mobile.presentation.client.main.search.SearchSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface SearchIntent {
    data class OnSearchQueryChanged(val query: String) :
        az.less.mobile.presentation.client.main.search.SearchIntent
    data class OnCategorySelected(val categoryId: String) :
        az.less.mobile.presentation.client.main.search.SearchIntent
    data class OnOfferClicked(val offerId: String) :
        az.less.mobile.presentation.client.main.search.SearchIntent
    data object OnBackClicked : az.less.mobile.presentation.client.main.search.SearchIntent
    data object OnMapClicked : az.less.mobile.presentation.client.main.search.SearchIntent
}

