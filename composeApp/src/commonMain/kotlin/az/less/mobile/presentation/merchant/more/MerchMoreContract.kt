package az.less.mobile.presentation.merchant.more

import az.less.mobile.presentation.merchant.more.model.MerchCellId
import az.less.mobile.presentation.merchant.more.model.MerchMoreSection


/**
 * State of the MerchMore Screen
 */
data class MerchMoreState(
    // Merchant info
    val merchantName: String = "",
    val merchantEmail: String = "",
    val rating: String = "0.0",
    val reviewCount: String = "0",
    // Settings
    val notificationEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isLoading: Boolean = false,
    // Sections list
    val sections: List<MerchMoreSection> = emptyList(),
    // Bottom sheets
    val showContactUsBottomSheet: Boolean = false,
    val showTermsBottomSheet: Boolean = false
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface MerchMoreSideEffect {
    data object NavigateToPlaces : MerchMoreSideEffect
    data object Logout : MerchMoreSideEffect
    data class ShowError(val message: String) : MerchMoreSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface MerchMoreIntent {
    data class OnCellClick(val cellId: MerchCellId) : MerchMoreIntent
    data object OnNotificationToggleClick : MerchMoreIntent
    data object OnDarkModeToggleClick : MerchMoreIntent
    data object OnContactUsDismiss : MerchMoreIntent
    data class OnContactUsItemClick(val itemId: String) : MerchMoreIntent
    data object OnTermsDismiss : MerchMoreIntent
    data object OnLogoutClicked : MerchMoreIntent
}
