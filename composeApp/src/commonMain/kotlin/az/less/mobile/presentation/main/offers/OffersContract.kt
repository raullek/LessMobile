package az.less.mobile.presentation.main.offers

import az.less.mobile.presentation.main.offers.models.Category
import az.less.mobile.presentation.main.offers.models.FilterSegment
import az.less.mobile.presentation.main.offers.models.OfferSection
import az.less.mobile.presentation.main.offers.models.SpecialDiscountItem

/**
 * State of the Offers Screen
 */
data class OffersState(
    val userName: String = "Katheryn",
    val userAvatarUrl: String? = null,
    val searchQuery: String = "",
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val specialDiscounts: List<SpecialDiscountItem> = emptyList(),
    val filterSegments: List<FilterSegment> = emptyList(),
    val selectedFilterSegmentId: String? = null,
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
    data object NavigateToReserve : OffersSideEffect
    data object NavigateToCategoryOffers : OffersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface OffersIntent {
    data class OnSearchQueryChanged(val query: String) : OffersIntent
    data object OnSearchClicked : OffersIntent
    data class OnCategorySelected(val categoryId: String) : OffersIntent
    data class OnFilterSegmentSelected(val segmentId: String) : OffersIntent
    data class OnOfferItemClicked(val offerId: String) : OffersIntent
    data class OnSeeAllClicked(val sectionId: String) : OffersIntent
}

