package az.less.mobile.presentation.main.more

import az.less.mobile.presentation.main.more.models.CellId
import az.less.mobile.presentation.main.more.models.MoreSection

/**
 * State of the More Screen
 */
data class MoreState(
    val isLoggedIn: Boolean = false,
    val userName: String? = null,
    val userEmail: String? = null,
    val userAvatarUrl: String? = null,
    val notificationEnabled: Boolean = true,
    val isLoading: Boolean = false,
    // Ecology stats
    val co2Saved: String? = null,
    val moneySaved: String? = null,
    val ecoHeroTitle: String? = null,
    val ecoHeroDescription: String? = null,
    // Sections list
    val sections: List<MoreSection> = emptyList()
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface MoreSideEffect {
    data object NavigateToLogin : MoreSideEffect
    data object NavigateToAccount : MoreSideEffect
    data object NavigateToPaymentMethods : MoreSideEffect
    data object NavigateToVoucher : MoreSideEffect
    data object NavigateToHistory : MoreSideEffect
    data object NavigateToSettings : MoreSideEffect
    data object NavigateToContactUs : MoreSideEffect
    data object NavigateToSignStore : MoreSideEffect
    data object NavigateToTermsOfService : MoreSideEffect
    data object NavigateToHowToUse : MoreSideEffect
    data class ShowError(val message: String) : MoreSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface MoreIntent {
    data object OnLoginClicked : MoreIntent
    data object OnLogoutClicked : MoreIntent
    data class OnCellClick(val cellId: CellId) : MoreIntent
    data object OnNotificationToggleClick : MoreIntent
}
