package az.less.mobile.presentation.merchant.more

import az.less.mobile.presentation.merchant.more.model.MerchCellId
import az.less.mobile.presentation.merchant.more.model.MerchMoreSection


/**
 * State of the MerchMore Screen
 */
data class MerchMoreState(
    // Venue info
    val venueName: String = "",
    val venueLogoUrl: String? = null,
    val rating: String = "0.0",
    val reviewCount: String = "0",
    val canManageBranches: Boolean = false,
    val canSwitchMode: Boolean = false,
    val isAlsoPartner: Boolean = false,
    // Settings
    val notificationEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isLoading: Boolean = false,
    // Sections list
    val sections: List<MerchMoreSection> = emptyList(),
    // Bottom sheets
    val showContactUsBottomSheet: Boolean = false,
    val showTermsBottomSheet: Boolean = false,
    // Terms content
    val termsTitle: String? = null,
    val termsContent: String? = null,
    val isTermsHtml: Boolean = false,
    val isTermsLoading: Boolean = false
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface MerchMoreSideEffect {
    data object NavigateToPlaces : MerchMoreSideEffect
    data object NavigateToClientFlow : MerchMoreSideEffect
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
