package az.less.mobile.presentation.partner.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.ContentRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.domain.usecase.RefreshUserProfileUseCase
import az.less.mobile.presentation.theme.ThemeManager
import az.less.mobile.presentation.partner.more.model.PartnerMoreCellId
import az.less.mobile.presentation.partner.more.model.PartnerMoreCellModel
import az.less.mobile.presentation.partner.more.model.PartnerMoreCellType
import az.less.mobile.presentation.partner.more.model.PartnerMoreSection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_customer_support_24dp
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_more_24dp
import lessmobile.composeapp.generated.resources.ic_notification_24dp
import lessmobile.composeapp.generated.resources.ic_terms_file_24dp
import lessmobile.composeapp.generated.resources.merch_more_dark_mode
import lessmobile.composeapp.generated.resources.more_contact_us
import lessmobile.composeapp.generated.resources.more_notification
import lessmobile.composeapp.generated.resources.more_section_application
import lessmobile.composeapp.generated.resources.more_section_support
import lessmobile.composeapp.generated.resources.more_switch_to_client
import lessmobile.composeapp.generated.resources.more_terms_of_service
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class PartnerMoreViewModel(
    private val sessionLocalRepository: SessionLocalRepository,
    private val authorizationRepository: AuthorizationRepository,
    private val contentRepository: ContentRepository,
    private val refreshUserProfile: RefreshUserProfileUseCase,
    private val themeManager: ThemeManager
) : ViewModel(), ContainerHost<PartnerMoreState, PartnerMoreSideEffect> {

    override val container: Container<PartnerMoreState, PartnerMoreSideEffect> =
        viewModelScope.container(PartnerMoreState())

    init {
        observeUserState()
        observeDarkMode()
        refreshUserInBackground()
    }

    private fun observeDarkMode() {
        viewModelScope.launch {
            themeManager.isDarkMode.collectLatest { isDark ->
                intent {
                    reduce {
                        state.copy(
                            darkModeEnabled = isDark,
                            sections = buildSections(
                                notificationEnabled = state.notificationEnabled,
                                darkModeEnabled = isDark,
                                canSwitchMode = state.canSwitchMode
                            )
                        )
                    }
                }
            }
        }
    }

    private fun refreshUserInBackground() {
        viewModelScope.launch { refreshUserProfile() }
    }

    private fun observeUserState() {
        viewModelScope.launch {
            sessionLocalRepository.currentUser.collectLatest { user ->
                intent {
                    if (user != null) {
                        reduce {
                            state.copy(
                                venueName = user.venue?.name ?: user.name,
                                venueLogoUrl = user.venue?.businessLogo,
                                canSwitchMode = user.canSwitchMode(),
                                sections = buildSections(
                                    notificationEnabled = state.notificationEnabled,
                                    darkModeEnabled = state.darkModeEnabled,
                                    canSwitchMode = user.canSwitchMode()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun onIntent(intent: PartnerMoreIntent) {
        when (intent) {
            is PartnerMoreIntent.OnCellClick -> handleCellClick(intent.cellId)
            is PartnerMoreIntent.OnNotificationToggleClick -> handleNotificationToggleClick()
            is PartnerMoreIntent.OnDarkModeToggleClick -> handleDarkModeToggleClick()
            is PartnerMoreIntent.OnContactUsDismiss -> handleContactUsDismiss()
            is PartnerMoreIntent.OnContactUsItemClick -> handleContactUsItemClick(intent.itemId)
            is PartnerMoreIntent.OnTermsDismiss -> handleTermsDismiss()
            is PartnerMoreIntent.OnLogoutClicked -> handleLogoutClicked()
        }
    }

    private fun handleCellClick(cellId: PartnerMoreCellId) = intent {
        when (cellId) {
            PartnerMoreCellId.ContactUs -> {
                reduce { state.copy(showContactUsBottomSheet = true) }
            }
            PartnerMoreCellId.TermsOfService -> fetchTermsAndShow()
            PartnerMoreCellId.Notification -> { }
            PartnerMoreCellId.DarkMode -> { }
            PartnerMoreCellId.SwitchToClient -> postSideEffect(PartnerMoreSideEffect.NavigateToClientFlow)
        }
    }

    private fun handleContactUsDismiss() = intent {
        reduce { state.copy(showContactUsBottomSheet = false) }
    }

    private fun handleContactUsItemClick(itemId: String) = intent {
        reduce { state.copy(showContactUsBottomSheet = false) }
    }

    private fun fetchTermsAndShow() = intent {
        reduce { state.copy(showTermsBottomSheet = true, isTermsLoading = true) }

        contentRepository.getTerms()
            .onSuccess { data ->
                reduce {
                    state.copy(
                        isTermsLoading = false,
                        termsTitle = data.title,
                        termsContent = data.body,
                        isTermsHtml = data.isHtml
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(showTermsBottomSheet = false, isTermsLoading = false) }
                postSideEffect(PartnerMoreSideEffect.ShowError(error.message))
            }
    }

    private fun handleTermsDismiss() = intent {
        reduce { state.copy(showTermsBottomSheet = false) }
    }

    private fun handleNotificationToggleClick() = intent {
        val newEnabled = !state.notificationEnabled
        reduce {
            state.copy(
                notificationEnabled = newEnabled,
                sections = buildSections(newEnabled, state.darkModeEnabled, canSwitchMode = state.canSwitchMode)
            )
        }
    }

    private fun handleDarkModeToggleClick() {
        themeManager.toggleDarkMode(viewModelScope)
    }

    private fun handleLogoutClicked() = intent {
        reduce { state.copy(isLoading = true) }

        val refreshToken = sessionLocalRepository.getRefreshToken()
        if (refreshToken != null) {
            authorizationRepository.logout(refreshToken)
        }

        sessionLocalRepository.clearSession()
        postSideEffect(PartnerMoreSideEffect.Logout)
    }

    private fun buildSections(
        notificationEnabled: Boolean,
        darkModeEnabled: Boolean,
        canSwitchMode: Boolean
    ): List<PartnerMoreSection> {
        val appCells = mutableListOf<PartnerMoreCellModel>()

        appCells.add(
            PartnerMoreCellModel(
                id = PartnerMoreCellId.Notification,
                titleRes = Res.string.more_notification,
                icon = Res.drawable.ic_notification_24dp,
                type = PartnerMoreCellType.Toggle(notificationEnabled)
            )
        )

        appCells.add(
            PartnerMoreCellModel(
                id = PartnerMoreCellId.DarkMode,
                titleRes = Res.string.merch_more_dark_mode,
                icon = Res.drawable.ic_more_24dp,
                type = PartnerMoreCellType.Toggle(darkModeEnabled),
                showDivider = canSwitchMode
            )
        )

        if (canSwitchMode) {
            appCells.add(
                PartnerMoreCellModel(
                    id = PartnerMoreCellId.SwitchToClient,
                    titleRes = Res.string.more_switch_to_client,
                    icon = Res.drawable.ic_explore_24dp,
                    type = PartnerMoreCellType.Navigation,
                    showDivider = false
                )
            )
        }

        return listOf(
            PartnerMoreSection(
                titleRes = Res.string.more_section_application,
                cells = appCells
            ),
            PartnerMoreSection(
                titleRes = Res.string.more_section_support,
                cells = listOf(
                    PartnerMoreCellModel(
                        id = PartnerMoreCellId.ContactUs,
                        titleRes = Res.string.more_contact_us,
                        icon = Res.drawable.ic_customer_support_24dp,
                        type = PartnerMoreCellType.Navigation
                    ),
                    PartnerMoreCellModel(
                        id = PartnerMoreCellId.TermsOfService,
                        titleRes = Res.string.more_terms_of_service,
                        icon = Res.drawable.ic_terms_file_24dp,
                        type = PartnerMoreCellType.Navigation,
                        showDivider = false
                    )
                )
            )
        )
    }
}
