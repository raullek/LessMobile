package az.less.mobile.presentation.main.offers

import az.less.mobile.presentation.main.offers.models.Category
import az.less.mobile.presentation.main.offers.models.OfferSection

/**
 * State of the Offers Screen
 */
data class OffersState(
    val userName: String = "Katheryn",
    val userAvatarUrl: String? = null,
    val searchQuery: String = "",
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val offerSections: List<OfferSection> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface OffersSideEffect {
    data class ShowError(val message: String) : OffersSideEffect
    data class NavigateToOfferDetail(val offerId: String) : OffersSideEffect
    data object NavigateToSearch : OffersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface OffersIntent {
    data class OnSearchQueryChanged(val query: String) : OffersIntent
    data object OnSearchClicked : OffersIntent
    data class OnCategorySelected(val categoryId: String) : OffersIntent
    data class OnOfferItemClicked(val offerId: String) : OffersIntent
}

