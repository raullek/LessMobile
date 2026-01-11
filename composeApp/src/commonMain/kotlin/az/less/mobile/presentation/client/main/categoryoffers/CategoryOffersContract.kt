package az.less.mobile.presentation.client.main.categoryoffers

import az.less.mobile.presentation.client.main.offers.models.OfferItem
import az.less.mobile.presentation.client.main.offers.models.SegmentedCategory

/**
 * State of the Category Offers Screen
 */
data class CategoryOffersState(
    val categoryId: String = "",
    val categoryType: String = "",
    val categoryTitle: String = "",
    val segmentedCategories: List<SegmentedCategory> = emptyList(),
    val selectedSegmentId: String? = null,
    val offers: List<OfferItem> = emptyList(),
    val isLoading: Boolean = false
) {
    val isEmpty: Boolean get() = offers.isEmpty() && !isLoading

    // Backward compatibility
    val filterSegments: List<SegmentedCategory> get() = segmentedCategories
    val selectedFilterSegmentId: String? get() = selectedSegmentId
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface CategoryOffersSideEffect {
    data class ShowError(val message: String) : CategoryOffersSideEffect
    data class NavigateToReserve(val offerItem: OfferItem) : CategoryOffersSideEffect
    data object NavigateBack : CategoryOffersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface CategoryOffersIntent {
    data object OnBackClicked : CategoryOffersIntent
    data class OnSegmentSelected(val segmentId: String) : CategoryOffersIntent
    data class OnOfferItemClicked(val offerId: String) : CategoryOffersIntent
}

// Backward compatibility alias
@Deprecated("Use OnSegmentSelected", ReplaceWith("CategoryOffersIntent.OnSegmentSelected"))
typealias OnFilterSegmentSelected = CategoryOffersIntent.OnSegmentSelected
