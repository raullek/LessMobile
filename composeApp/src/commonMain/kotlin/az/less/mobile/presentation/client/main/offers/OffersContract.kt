package az.less.mobile.presentation.client.main.offers

import az.less.mobile.presentation.client.main.offers.models.Category
import az.less.mobile.presentation.client.main.offers.models.OfferSection
import az.less.mobile.presentation.client.main.offers.models.SegmentedCategory
import az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem
import az.less.mobile.presentation.client.main.offers.models.UserInfo

/**
 * State of the Offers Screen
 */
data class OffersState(
    // User info (separate request)
    val userInfo: UserInfo? = null,
    val isUserLoading: Boolean = false,

    // Screen data (single request)
    val categories: List<Category> = emptyList(),
    val specialCategories: List<SpecialDiscountItem> = emptyList(),
    val segmentedCategories: List<SegmentedCategory> = emptyList(),
    val offerSections: List<OfferSection> = emptyList(),
    val isLoading: Boolean = true, // Start with loading to show shimmer immediately

    // UI state
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val selectedSegmentId: String? = null
) {
    // Backward compatibility
    val userName: String get() = userInfo?.name ?: ""
    val userAvatarUrl: String? get() = userInfo?.avatarUrl
    val specialDiscounts: List<SpecialDiscountItem> get() = specialCategories
    val filterSegments: List<SegmentedCategory> get() = segmentedCategories
    val selectedFilterSegmentId: String? get() = selectedSegmentId
}

/**
 * Side Effects for navigation and one-time events
 */
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

/**
 * User Intents/Actions
 */
sealed interface OffersIntent {
    data class OnSearchQueryChanged(val query: String) : OffersIntent
    data object OnSearchClicked : OffersIntent
    data class OnCategorySelected(val categoryId: String) : OffersIntent
    data class OnSpecialCategoryClicked(val specialCategoryId: String) : OffersIntent
    data class OnSegmentSelected(val segmentId: String) : OffersIntent
    data class OnOfferItemClicked(val offerId: String) : OffersIntent
    data class OnSeeAllClicked(val sectionId: String) : OffersIntent
}

// Backward compatibility alias
@Deprecated("Use OnSegmentSelected", ReplaceWith("OffersIntent.OnSegmentSelected"))
typealias OnFilterSegmentSelected = OffersIntent.OnSegmentSelected
