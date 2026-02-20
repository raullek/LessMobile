package az.less.mobile.presentation.client.main.offers

import az.less.mobile.presentation.client.main.offers.models.Category
import az.less.mobile.presentation.client.main.offers.models.OfferSection
import az.less.mobile.presentation.client.main.offers.models.SegmentedCategory
import az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem
import az.less.mobile.presentation.client.main.offers.models.UserInfo

data class OffersState(
    val userInfo: UserInfo? = null,
    val isUserLoading: Boolean = false,

    val latitude: Double? = null,
    val longitude: Double? = null,

    val categories: List<Category> = emptyList(),
    val specialCategories: List<SpecialDiscountItem> = emptyList(),
    val segmentedCategories: List<SegmentedCategory> = emptyList(),
    val offerSections: List<OfferSection> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,

    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val selectedSegmentId: String? = null
) {
    val userName: String? get() = userInfo?.name?.takeIf { it.isNotBlank() }
    val userAvatarUrl: String? get() = userInfo?.avatarUrl
}

sealed interface OffersSideEffect {
    data class ShowError(val message: String) : OffersSideEffect
    data class NavigateToOfferDetail(val offerId: String) : OffersSideEffect
    data object NavigateToSearch : OffersSideEffect
    data object NavigateToReserve : OffersSideEffect
    data class NavigateToCategoryOffers(
        val categoryId: String,
        val categoryType: String,
        val categoryTitle: String
    ) : OffersSideEffect
}

sealed interface OffersIntent {
    data class OnSearchQueryChanged(val query: String) : OffersIntent
    data object OnSearchClicked : OffersIntent
    data class OnCategorySelected(val categoryId: String) : OffersIntent
    data class OnSpecialCategoryClicked(val specialCategoryId: String) : OffersIntent
    data class OnSegmentSelected(val segmentId: String) : OffersIntent
    data class OnOfferItemClicked(val offerId: String) : OffersIntent
    data class OnSeeAllClicked(val sectionId: String) : OffersIntent
    data object OnRefresh : OffersIntent
}
