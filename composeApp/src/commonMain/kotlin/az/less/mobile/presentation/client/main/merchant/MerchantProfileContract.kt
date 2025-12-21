package az.less.mobile.presentation.client.main.merchant

import az.less.mobile.presentation.client.main.merchant.models.MerchantOfferItem

/**
 * State of the Merchant Profile Screen
 */
data class MerchantProfileState(
    val merchantId: String = "",
    val merchantName: String = "",
    val merchantDescription: String = "",
    val merchantLogoUrl: String? = null,
    val heroImageUrl: String? = null,
    val rating: Float = 0f,
    val distance: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isFavorite: Boolean = false,
    val isMapVisible: Boolean = false,
    val selectedTab: MerchantProfileTab = MerchantProfileTab.OFFERS,
    val offers: List<MerchantOfferItem> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * Tabs on the Merchant Profile screen
 */
enum class MerchantProfileTab {
    OFFERS,
    REVIEWS
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface MerchantProfileSideEffect {
    data class ShowError(val message: String) : MerchantProfileSideEffect
    data class NavigateToReserve(val offerId: String) : MerchantProfileSideEffect
    data object NavigateBack : MerchantProfileSideEffect
    data class OpenDirections(val address: String) : MerchantProfileSideEffect
    data class CallPhone(val phoneNumber: String) : MerchantProfileSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface MerchantProfileIntent {
    data object OnBackClicked : MerchantProfileIntent
    data object OnFavoriteClicked : MerchantProfileIntent
    data class OnTabSelected(val tab: MerchantProfileTab) : MerchantProfileIntent
    data class OnOfferClicked(val offerId: String) : MerchantProfileIntent
    data object OnDirectionsClicked : MerchantProfileIntent
    data object OnPhoneClicked : MerchantProfileIntent
    data object OnViewLocationClicked : MerchantProfileIntent
    data object OnMapBackClicked : MerchantProfileIntent
}
