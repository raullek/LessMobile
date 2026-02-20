package az.less.mobile.presentation.client.main.categoryoffers

import az.less.mobile.presentation.client.main.offers.models.OfferItem
import az.less.mobile.presentation.client.main.offers.models.SegmentedCategory

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
}

sealed interface CategoryOffersSideEffect {
    data class ShowError(val message: String) : CategoryOffersSideEffect
    data class NavigateToReserve(val offerItem: OfferItem) : CategoryOffersSideEffect
    data object NavigateBack : CategoryOffersSideEffect
}

sealed interface CategoryOffersIntent {
    data object OnBackClicked : CategoryOffersIntent
    data class OnSegmentSelected(val segmentId: String) : CategoryOffersIntent
    data class OnOfferItemClicked(val offerId: String) : CategoryOffersIntent
}
