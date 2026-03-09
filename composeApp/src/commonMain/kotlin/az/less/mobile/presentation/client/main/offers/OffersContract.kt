package az.less.mobile.presentation.client.main.offers

import az.less.mobile.presentation.client.main.offers.models.Category
import az.less.mobile.presentation.client.main.offers.models.HomepageButton
import az.less.mobile.presentation.client.main.offers.models.OfferSection
import az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem
import az.less.mobile.presentation.client.main.offers.models.UserInfo

data class OffersState(
    val userInfo: UserInfo? = null,
    val isUserLoading: Boolean = false,

    val categories: List<Category> = emptyList(),
    val specialCategories: List<SpecialDiscountItem> = emptyList(),
    val homepageButtons: List<HomepageButton> = emptyList(),
    val specialSegments: List<OfferSection> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,

    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val selectedSegmentId: String? = null,
    val locationPermissionGranted: Boolean = false
) {
    val userName: String? get() = userInfo?.name?.takeIf { it.isNotBlank() }
    val userAvatarUrl: String? get() = userInfo?.avatarUrl
}

sealed interface OffersSideEffect {
    data class ShowError(val message: String) : OffersSideEffect
    data class NavigateToOfferDetail(val offerId: String) : OffersSideEffect
    data object NavigateToSearch : OffersSideEffect
    data class NavigateToReserve(val offerId: String) : OffersSideEffect
    data class NavigateToCategoryOffers(
        val categoryId: String,
        val categoryType: String,
        val categoryTitle: String,
        val filtersJson: String
    ) : OffersSideEffect
}

sealed interface OffersIntent {
    data class OnLocationPermissionChanged(val granted: Boolean) : OffersIntent
    data class OnSearchQueryChanged(val query: String) : OffersIntent
    data object OnSearchClicked : OffersIntent
    data class OnCategorySelected(val categoryId: String) : OffersIntent
    data class OnSpecialCategoryClicked(val specialCategoryId: String) : OffersIntent
    data class OnHomepageButtonClicked(val buttonId: String) : OffersIntent
    data class OnOfferItemClicked(val offerId: String) : OffersIntent
    data class OnSeeAllClicked(val sectionId: String) : OffersIntent
    data object OnRefresh : OffersIntent
}
