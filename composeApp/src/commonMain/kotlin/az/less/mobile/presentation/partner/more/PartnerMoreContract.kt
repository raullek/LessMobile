package az.less.mobile.presentation.partner.more

import az.less.mobile.presentation.partner.more.model.PartnerMoreCellId
import az.less.mobile.presentation.partner.more.model.PartnerMoreSection

data class PartnerMoreState(
    val venueName: String = "",
    val venueLogoUrl: String? = null,
    val canSwitchMode: Boolean = false,
    val notificationEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val sections: List<PartnerMoreSection> = emptyList(),
    val showContactUsBottomSheet: Boolean = false,
    val showTermsBottomSheet: Boolean = false,
    val termsTitle: String? = null,
    val termsContent: String? = null,
    val isTermsHtml: Boolean = false,
    val isTermsLoading: Boolean = false
)

sealed interface PartnerMoreSideEffect {
    data object NavigateToClientFlow : PartnerMoreSideEffect
    data object Logout : PartnerMoreSideEffect
    data class ShowError(val message: String) : PartnerMoreSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface PartnerMoreIntent {
    data class OnCellClick(val cellId: PartnerMoreCellId) : PartnerMoreIntent
    data object OnNotificationToggleClick : PartnerMoreIntent
    data object OnDarkModeToggleClick : PartnerMoreIntent
    data object OnContactUsDismiss : PartnerMoreIntent
    data class OnContactUsItemClick(val itemId: String) : PartnerMoreIntent
    data object OnTermsDismiss : PartnerMoreIntent
    data object OnLogoutClicked : PartnerMoreIntent
}
