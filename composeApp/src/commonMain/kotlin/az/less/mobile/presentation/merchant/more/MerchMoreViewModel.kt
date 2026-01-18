package az.less.mobile.presentation.merchant.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.merchant.more.model.MerchCellId
import az.less.mobile.presentation.merchant.more.model.MerchMoreCellModel
import az.less.mobile.presentation.merchant.more.model.MerchMoreCellType
import az.less.mobile.presentation.merchant.more.model.MerchMoreSection
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_customer_support_24dp
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_more_24dp
import lessmobile.composeapp.generated.resources.ic_notification_24dp
import lessmobile.composeapp.generated.resources.ic_terms_file_24dp
import lessmobile.composeapp.generated.resources.merch_more_dark_mode
import lessmobile.composeapp.generated.resources.merch_more_places
import lessmobile.composeapp.generated.resources.more_contact_us
import lessmobile.composeapp.generated.resources.more_notification
import lessmobile.composeapp.generated.resources.more_section_application
import lessmobile.composeapp.generated.resources.more_section_support
import lessmobile.composeapp.generated.resources.more_terms_of_service
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for MerchMore Screen using Orbit MVI
 */
class MerchMoreViewModel : ViewModel(), ContainerHost<MerchMoreState, MerchMoreSideEffect> {

    override val container: Container<MerchMoreState, MerchMoreSideEffect> =
        viewModelScope.container(MerchMoreState())

    init {
        loadMerchantData()
    }

    private fun loadMerchantData() = intent {
        // TODO: Load merchant data from repository
        // For now, using mock data
        reduce {
            state.copy(
                merchantName = "McDonald's",
                merchantEmail = "Ahmadli@mcdonald.az",
                rating = "4.9",
                reviewCount = "28+",
                sections = buildSections(state.notificationEnabled, state.darkModeEnabled)
            )
        }
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: MerchMoreIntent) {
        when (intent) {
            is MerchMoreIntent.OnCellClick -> handleCellClick(intent.cellId)
            is MerchMoreIntent.OnNotificationToggleClick -> handleNotificationToggleClick()
            is MerchMoreIntent.OnDarkModeToggleClick -> handleDarkModeToggleClick()
            is MerchMoreIntent.OnContactUsDismiss -> handleContactUsDismiss()
            is MerchMoreIntent.OnContactUsItemClick -> handleContactUsItemClick(intent.itemId)
            is MerchMoreIntent.OnTermsDismiss -> handleTermsDismiss()
            is MerchMoreIntent.OnLogoutClicked -> handleLogoutClicked()
        }
    }

    private fun handleCellClick(cellId: MerchCellId) = intent {
        when (cellId) {
            MerchCellId.Places -> postSideEffect(MerchMoreSideEffect.NavigateToPlaces)
            MerchCellId.ContactUs -> {
                reduce {
                    state.copy(showContactUsBottomSheet = true)
                }
            }
            MerchCellId.TermsOfService -> {
                reduce {
                    state.copy(showTermsBottomSheet = true)
                }
            }
            MerchCellId.Notification -> {
                // Notification is handled separately via toggle
            }
            MerchCellId.DarkMode -> {
                // Dark mode is handled separately via toggle
            }
        }
    }

    private fun handleContactUsDismiss() = intent {
        reduce {
            state.copy(showContactUsBottomSheet = false)
        }
    }

    private fun handleContactUsItemClick(itemId: String) = intent {
        // TODO: Handle contact item click (open Instagram, TikTok, etc.)
        reduce {
            state.copy(showContactUsBottomSheet = false)
        }
    }

    private fun handleTermsDismiss() = intent {
        reduce {
            state.copy(showTermsBottomSheet = false)
        }
    }

    private fun handleNotificationToggleClick() = intent {
        val newEnabled = !state.notificationEnabled
        reduce {
            state.copy(
                notificationEnabled = newEnabled,
                sections = buildSections(newEnabled, state.darkModeEnabled)
            )
        }
    }

    private fun handleDarkModeToggleClick() = intent {
        val newEnabled = !state.darkModeEnabled
        reduce {
            state.copy(
                darkModeEnabled = newEnabled,
                sections = buildSections(state.notificationEnabled, newEnabled)
            )
        }
    }

    private fun handleLogoutClicked() = intent {
        postSideEffect(MerchMoreSideEffect.Logout)
    }

    /**
     * Build sections for merchant More screen
     * Application: Places, Notification, Dark mode
     * Support: Contact us, Terms of Service
     */
    private fun buildSections(notificationEnabled: Boolean, darkModeEnabled: Boolean): List<MerchMoreSection> {
        return listOf(
            MerchMoreSection(
                titleRes = Res.string.more_section_application,
                cells = listOf(
                    MerchMoreCellModel(
                        id = MerchCellId.Places,
                        titleRes = Res.string.merch_more_places,
                        icon = Res.drawable.ic_explore_24dp,
                        type = MerchMoreCellType.Navigation
                    ),
                    MerchMoreCellModel(
                        id = MerchCellId.Notification,
                        titleRes = Res.string.more_notification,
                        icon = Res.drawable.ic_notification_24dp,
                        type = MerchMoreCellType.Toggle(notificationEnabled)
                    ),
                    MerchMoreCellModel(
                        id = MerchCellId.DarkMode,
                        titleRes = Res.string.merch_more_dark_mode,
                        icon = Res.drawable.ic_more_24dp,
                        type = MerchMoreCellType.Toggle(darkModeEnabled),
                        showDivider = false
                    )
                )
            ),
            MerchMoreSection(
                titleRes = Res.string.more_section_support,
                cells = listOf(
                    MerchMoreCellModel(
                        id = MerchCellId.ContactUs,
                        titleRes = Res.string.more_contact_us,
                        icon = Res.drawable.ic_customer_support_24dp,
                        type = MerchMoreCellType.Navigation
                    ),
                    MerchMoreCellModel(
                        id = MerchCellId.TermsOfService,
                        titleRes = Res.string.more_terms_of_service,
                        icon = Res.drawable.ic_terms_file_24dp,
                        type = MerchMoreCellType.Navigation,
                        showDivider = false
                    )
                )
            )
        )
    }
}
