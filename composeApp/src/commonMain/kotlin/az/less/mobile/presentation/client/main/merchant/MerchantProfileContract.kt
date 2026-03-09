package az.less.mobile.presentation.client.main.merchant

import az.less.mobile.domain.model.MerchantOffer
import az.less.mobile.domain.model.MerchantReview

data class MerchantProfileState(
    val merchantId: String = "",
    val merchantName: String = "",
    val merchantDescription: String = "",
    val merchantLogoUrl: String? = null,
    val heroImageUrl: String? = null,
    val rating: Float = 0f,
    val ratingCount: Int = 0,
    val ratingDistribution: Map<Int, Int> = emptyMap(),
    val distance: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isFavorite: Boolean = false,
    val favoriteId: String? = null,
    val isMapVisible: Boolean = false,
    val selectedTab: MerchantProfileTab = MerchantProfileTab.OFFERS,
    val offers: List<MerchantOffer> = emptyList(),
    val reviews: List<MerchantReview> = emptyList(),
    val reviewsTotal: Int = 0,
    val reviewsHasMore: Boolean = false,
    val isLoading: Boolean = false
)

enum class MerchantProfileTab {
    OFFERS,
    REVIEWS
}

sealed interface MerchantProfileSideEffect {
    data class ShowError(val message: String) : MerchantProfileSideEffect
    data object NavigateBack : MerchantProfileSideEffect
    data class OpenDirections(val address: String) : MerchantProfileSideEffect
    data class CallPhone(val phoneNumber: String) : MerchantProfileSideEffect
    data class NavigateToReserve(val offerId: String) : MerchantProfileSideEffect
}

sealed interface MerchantProfileIntent {
    data object OnBackClicked : MerchantProfileIntent
    data object OnFavoriteClicked : MerchantProfileIntent
    data class OnTabSelected(val tab: MerchantProfileTab) : MerchantProfileIntent
    data object OnDirectionsClicked : MerchantProfileIntent
    data object OnPhoneClicked : MerchantProfileIntent
    data object OnViewLocationClicked : MerchantProfileIntent
    data object OnMapBackClicked : MerchantProfileIntent
    data class OnOfferClicked(val offerId: String) : MerchantProfileIntent
}
