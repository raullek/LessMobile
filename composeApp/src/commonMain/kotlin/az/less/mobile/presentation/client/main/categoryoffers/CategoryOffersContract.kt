package az.less.mobile.presentation.client.main.categoryoffers

import az.less.mobile.presentation.client.main.offers.models.FilterSegment
import az.less.mobile.presentation.client.main.offers.models.OfferItem

/**
 * State of the Category Offers Screen
 */
data class CategoryOffersState(
    val categoryType: String = "",
    val categoryTitle: String = "",
    val filterSegments: List<FilterSegment> = emptyList(),
    val selectedFilterSegmentId: String? = null,
    val offers: List<OfferItem> = emptyList(),
    val isLoading: Boolean = false
) {
    val isEmpty: Boolean get() = offers.isEmpty() && !isLoading
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface CategoryOffersSideEffect {
    data class ShowError(val message: String) :
        CategoryOffersSideEffect
    data class NavigateToReserve(val offerItem: OfferItem) :
        CategoryOffersSideEffect
    data object NavigateBack :
        CategoryOffersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface CategoryOffersIntent {
    data object OnBackClicked :
        CategoryOffersIntent
    data class OnFilterSegmentSelected(val segmentId: String) :
        CategoryOffersIntent
    data class OnOfferItemClicked(val offerId: String) :
        CategoryOffersIntent
}

