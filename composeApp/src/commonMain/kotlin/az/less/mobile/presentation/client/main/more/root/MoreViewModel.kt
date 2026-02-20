package az.less.mobile.presentation.client.main.more.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.presentation.client.main.more.root.models.CellId
import az.less.mobile.presentation.client.main.more.root.models.MoreCellModel
import az.less.mobile.presentation.client.main.more.root.models.MoreCellType
import az.less.mobile.presentation.client.main.more.root.models.MoreSection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
class MoreViewModel(
    private val userLocalRepository: SessionLocalRepository,
    private val authorizationRepository: AuthorizationRepository
) : ViewModel(), ContainerHost<MoreState, MoreSideEffect> {

    override val container: Container<MoreState, MoreSideEffect> =
        viewModelScope.container(MoreState())

    init {
        observeUserState()
    }

    private fun observeUserState() {
        viewModelScope.launch {
            userLocalRepository.currentUser.collectLatest { user ->
                intent {
                    if (user != null) {
                        reduce {
                            state.copy(
                                isLoggedIn = true,
                                userName = user.name,
                                userEmail = user.email,
                                sections = buildAuthSections(state.notificationEnabled)
                            )
                        }
                    } else {
                        reduce {
                            state.copy(
                                isLoggedIn = false,
                                userName = null,
                                userEmail = null,
                                sections = buildNonAuthSections(state.notificationEnabled)
                            )
                        }
                    }
                }
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
        // Navigate to onboarding/login flow
        postSideEffect(MoreSideEffect.NavigateToLogin)
    }

    private fun handleLogoutClicked() = intent {
        reduce { state.copy(isLoading = true) }

        val refreshToken = userLocalRepository.getRefreshToken()
        if (refreshToken != null) {
            authorizationRepository.logout(refreshToken)
        }

        // Always clear session locally regardless of API result
        userLocalRepository.clearSession()
        // State will be updated automatically via observeUserState()
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
