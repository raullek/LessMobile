package az.less.mobile.presentation.partner.preview

import az.less.mobile.domain.model.MerchantOffer
import az.less.mobile.domain.model.MerchantReview
import az.less.mobile.presentation.client.main.merchant.MerchantProfileTab

/**
 * Read-only state for the venue preview shown to merchants/partners.
 * Mirrors the public-facing client merchant profile but exposes no actions
 * that mutate or navigate further (no favorite, no reserve).
 */
data class VenuePreviewState(
    val venueId: String = "",
    val merchantName: String = "",
    val merchantDescription: String = "",
    val merchantLogoUrl: String? = null,
    val heroImageUrl: String? = null,
    val rating: Float = 0f,
    val ratingCount: Int = 0,
    val ratingDistribution: Map<Int, Int> = emptyMap(),
    val phoneNumber: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isMapVisible: Boolean = false,
    val selectedTab: MerchantProfileTab = MerchantProfileTab.OFFERS,
    val offers: List<MerchantOffer> = emptyList(),
    val reviews: List<MerchantReview> = emptyList(),
    val reviewsTotal: Int = 0,
    val isLoading: Boolean = false
)

sealed interface VenuePreviewSideEffect {
    data class ShowError(val message: String) : VenuePreviewSideEffect
    data object NavigateBack : VenuePreviewSideEffect
}

sealed interface VenuePreviewIntent {
    data object OnBackClicked : VenuePreviewIntent
    data class OnTabSelected(val tab: MerchantProfileTab) : VenuePreviewIntent
    data object OnViewLocationClicked : VenuePreviewIntent
    data object OnMapBackClicked : VenuePreviewIntent
}
