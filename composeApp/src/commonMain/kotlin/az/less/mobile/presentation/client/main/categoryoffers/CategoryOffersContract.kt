package az.less.mobile.presentation.client.main.categoryoffers

import az.less.mobile.presentation.client.main.offers.models.OfferItem

data class CategoryOffersState(
    val categoryTitle: String = "",
    val filtersJson: String = "[]",
    val isLoading: Boolean = true
) {
    val isEmpty: Boolean get() = !isLoading
}

sealed interface CategoryOffersSideEffect {
    data class ShowError(val message: String) : CategoryOffersSideEffect
    data class NavigateToReserve(val offerItem: OfferItem) : CategoryOffersSideEffect
    data object NavigateBack : CategoryOffersSideEffect
}

sealed interface CategoryOffersIntent {
    data object OnBackClicked : CategoryOffersIntent
    data class OnOfferItemClicked(val offerId: String) : CategoryOffersIntent
}
