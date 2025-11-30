package az.less.mobile.presentation.main.more.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.main.more.root.models.CellId
import az.less.mobile.presentation.main.more.root.models.MoreCellModel
import az.less.mobile.presentation.main.more.root.models.MoreCellType
import az.less.mobile.presentation.main.more.root.models.MoreSection
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_account_24dp
import lessmobile.composeapp.generated.resources.ic_bubble_question_24dp
import lessmobile.composeapp.generated.resources.ic_clock_24dp
import lessmobile.composeapp.generated.resources.ic_customer_support_24dp
import lessmobile.composeapp.generated.resources.ic_notification_24dp
import lessmobile.composeapp.generated.resources.ic_payment_card_24dp
import lessmobile.composeapp.generated.resources.ic_terms_file_24dp
import lessmobile.composeapp.generated.resources.ic_voucher_24dp
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for More Screen using Orbit MVI
 */
class MoreViewModel : ViewModel(), ContainerHost<MoreState, MoreSideEffect> {
    
    override val container: Container<MoreState, MoreSideEffect> = 
        viewModelScope.container(MoreState())
    
    init {
        // Initialize with non-authenticated sections
        intent {
            reduce {
                state.copy(sections = buildNonAuthSections(state.notificationEnabled))
            }
        }
    }
    
    /**
     * Handle user intents
     */
    fun onIntent(intent: MoreIntent) {
        when (intent) {
            is MoreIntent.OnLoginClicked -> handleLoginClicked()
            is MoreIntent.OnLogoutClicked -> handleLogoutClicked()
            is MoreIntent.OnCellClick -> handleCellClick(intent.cellId)
            is MoreIntent.OnNotificationToggleClick -> handleNotificationToggleClick()
            is MoreIntent.OnContactUsDismiss -> handleContactUsDismiss()
            is MoreIntent.OnContactUsItemClick -> handleContactUsItemClick(intent.itemId)
            is MoreIntent.OnTermsDismiss -> handleTermsDismiss()
        }
    }
    
    private fun handleLoginClicked() = intent {
        // For now, just simulate login
        // In real app, navigate to login screen and handle authentication
        postSideEffect(MoreSideEffect.NavigateToLogin)
        
        // Mock login success - in real app this would come from auth repository
        reduce {
            state.copy(
                isLoggedIn = true,
                userName = "Maqa",
                userEmail = "Maqa@gmail.com",
                co2Saved = "60 kg",
                moneySaved = "$120",
                ecoHeroTitle = "Эко-герой",
                ecoHeroDescription = "Вы спасли 2 приёмов пищи!",
                sections = buildAuthSections(state.notificationEnabled)
            )
        }
    }
    
    private fun handleLogoutClicked() = intent {
        reduce {
            state.copy(
                isLoggedIn = false,
                userName = null,
                userEmail = null,
                userAvatarUrl = null,
                co2Saved = null,
                moneySaved = null,
                ecoHeroTitle = null,
                ecoHeroDescription = null,
                sections = buildNonAuthSections(state.notificationEnabled)
            )
        }
    }
    
    private fun handleCellClick(cellId: CellId) = intent {
        when (cellId) {
            CellId.Account -> postSideEffect(MoreSideEffect.NavigateToAccount)
            CellId.PaymentMethods -> postSideEffect(MoreSideEffect.NavigateToPaymentMethods)
            CellId.Voucher -> postSideEffect(MoreSideEffect.NavigateToVoucher)
            CellId.History -> postSideEffect(MoreSideEffect.NavigateToHistory)
            CellId.Settings -> postSideEffect(MoreSideEffect.NavigateToSettings)
            CellId.ContactUs -> {
                reduce {
                    state.copy(showContactUsBottomSheet = true)
                }
            }
            CellId.SignStore -> postSideEffect(MoreSideEffect.NavigateToSignStore)
            CellId.TermsOfService -> {
                reduce {
                    state.copy(showTermsBottomSheet = true)
                }
            }
            CellId.HowToUse -> postSideEffect(MoreSideEffect.NavigateToHowToUse)
            CellId.Notification -> {
                // Notification is handled separately via toggle
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
        // For now, just close the bottom sheet
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
                sections = if (state.isLoggedIn) {
                    buildAuthSections(newEnabled)
                } else {
                    buildNonAuthSections(newEnabled)
                }
            )
        }
        // In real app, save preference to repository
    }
    
    /**
     * Build sections for authenticated users
     */
    private fun buildAuthSections(notificationEnabled: Boolean): List<MoreSection> {
        return listOf(
            MoreSection(
                title = "Application",
                cells = listOf(
                    MoreCellModel(
                        id = CellId.Account,
                        title = "Account",
                        icon = Res.drawable.ic_account_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.PaymentMethods,
                        title = "Payment methods",
                        icon = Res.drawable.ic_payment_card_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.Voucher,
                        title = "Vouchers",
                        icon = Res.drawable.ic_voucher_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.History,
                        title = "History?",
                        icon = Res.drawable.ic_clock_24dp,
                        type = MoreCellType.Navigation,
                    ),
                    MoreCellModel(
                        id = CellId.Notification,
                        title = "Notification",
                        icon = Res.drawable.ic_notification_24dp,
                        type = MoreCellType.Toggle(notificationEnabled),
                        showDivider = false
                    )
                )
            ),
            MoreSection(
                title = "Support",
                cells = listOf(
                    MoreCellModel(
                        id = CellId.ContactUs,
                        title = "Contact us",
                        icon = Res.drawable.ic_customer_support_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.TermsOfService,
                        title = "Terms of Service",
                        icon = Res.drawable.ic_account_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.HowToUse,
                        title = "How to use",
                        icon = Res.drawable.ic_bubble_question_24dp,
                        type = MoreCellType.Navigation,
                        showDivider = false
                    )
                )
            )
        )
    }
    
    /**
     * Build sections for non-authenticated users
     */
    private fun buildNonAuthSections(notificationEnabled: Boolean): List<MoreSection> {
        return listOf(
            MoreSection(
                title = "Application",
                cells = listOf(
                    MoreCellModel(
                        id = CellId.Notification,
                        title = "Notification",
                        icon = Res.drawable.ic_notification_24dp,
                        type = MoreCellType.Toggle(notificationEnabled),
                        showDivider = false
                    )
                )
            ),
            MoreSection(
                title = "Support",
                cells = listOf(
                    MoreCellModel(
                        id = CellId.ContactUs,
                        title = "Contact us",
                        icon = Res.drawable.ic_account_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.TermsOfService,
                        title = "Terms of Service",
                        icon = Res.drawable.ic_terms_file_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.HowToUse,
                        title = "How to use",
                        icon = Res.drawable.ic_bubble_question_24dp,
                        type = MoreCellType.Navigation,
                        showDivider = false
                    )
                )
            )
        )
    }
}
