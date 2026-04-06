package az.less.mobile.presentation.client.main.more.root

import az.less.mobile.presentation.client.main.more.root.models.CellId
import az.less.mobile.presentation.client.main.more.root.models.MoreSection

/**
 * State of the More Screen
 */
data class MoreState(
    val isLoggedIn: Boolean = false,
    val canSwitchMode: Boolean = false,
    val userName: String? = null,
    val userEmail: String? = null,
    val userAvatarUrl: String? = null,
    val notificationEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isLoading: Boolean = false,
    // Ecology stats
    val co2Saved: String? = null,
    val moneySaved: String? = null,
    val ecoHeroTitle: String? = null,
    val ecoHeroDescription: String? = null,
    // Sections list
    val sections: List<az.less.mobile.presentation.client.main.more.root.models.MoreSection> = emptyList(),
    // Bottom sheets
    val locationPermissionGranted: Boolean = false,
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
sealed interface MoreSideEffect {
    data object NavigateToLogin : az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToAccount : az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToPaymentMethods :
        az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToVoucher : az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToSettings :
        az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToContactUs :
        az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToSignStore :
        az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToTermsOfService :
        az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToHowToUse :
        az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToAppSettings :
        az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data object NavigateToMerchantFlow :
        az.less.mobile.presentation.client.main.more.root.MoreSideEffect
    data class ShowError(val message: String) :
        az.less.mobile.presentation.client.main.more.root.MoreSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface MoreIntent {
    data object OnLoginClicked : az.less.mobile.presentation.client.main.more.root.MoreIntent
    data object OnLogoutClicked : az.less.mobile.presentation.client.main.more.root.MoreIntent
    data class OnCellClick(val cellId: az.less.mobile.presentation.client.main.more.root.models.CellId) :
        az.less.mobile.presentation.client.main.more.root.MoreIntent
    data object OnNotificationToggleClick :
        az.less.mobile.presentation.client.main.more.root.MoreIntent
    data object OnDarkModeToggleClick :
        az.less.mobile.presentation.client.main.more.root.MoreIntent
    data object OnContactUsDismiss : az.less.mobile.presentation.client.main.more.root.MoreIntent
    data class OnContactUsItemClick(val itemId: String) :
        az.less.mobile.presentation.client.main.more.root.MoreIntent
    data object OnTermsDismiss : az.less.mobile.presentation.client.main.more.root.MoreIntent
}
