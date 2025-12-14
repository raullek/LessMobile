package az.less.mobile.presentation.client.main.offers

import az.less.mobile.presentation.client.main.offers.models.Category
import az.less.mobile.presentation.client.main.offers.models.FilterSegment
import az.less.mobile.presentation.client.main.offers.models.OfferSection
import az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem

/**
 * State of the Offers Screen
 */
data class OffersState(
    val userName: String = "Katheryn",
    val userAvatarUrl: String? = null,
    val searchQuery: String = "",
    val categories: List<az.less.mobile.presentation.client.main.offers.models.Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val specialDiscounts: List<az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem> = emptyList(),
    val filterSegments: List<az.less.mobile.presentation.client.main.offers.models.FilterSegment> = emptyList(),
    val selectedFilterSegmentId: String? = null,
    val offerSections: List<az.less.mobile.presentation.client.main.offers.models.OfferSection> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface OffersSideEffect {
    data class ShowError(val message: String) :
        az.less.mobile.presentation.client.main.offers.OffersSideEffect
    data class NavigateToOfferDetail(val offerId: String) :
        az.less.mobile.presentation.client.main.offers.OffersSideEffect
    data object NavigateToSearch : az.less.mobile.presentation.client.main.offers.OffersSideEffect
    data object NavigateToReserve : az.less.mobile.presentation.client.main.offers.OffersSideEffect
    data object NavigateToCategoryOffers :
        az.less.mobile.presentation.client.main.offers.OffersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface OffersIntent {
    data class OnSearchQueryChanged(val query: String) :
        az.less.mobile.presentation.client.main.offers.OffersIntent
    data object OnSearchClicked : az.less.mobile.presentation.client.main.offers.OffersIntent
    data class OnCategorySelected(val categoryId: String) :
        az.less.mobile.presentation.client.main.offers.OffersIntent
    data class OnFilterSegmentSelected(val segmentId: String) :
        az.less.mobile.presentation.client.main.offers.OffersIntent
    data class OnOfferItemClicked(val offerId: String) :
        az.less.mobile.presentation.client.main.offers.OffersIntent
    data class OnSeeAllClicked(val sectionId: String) :
        az.less.mobile.presentation.client.main.offers.OffersIntent
}

