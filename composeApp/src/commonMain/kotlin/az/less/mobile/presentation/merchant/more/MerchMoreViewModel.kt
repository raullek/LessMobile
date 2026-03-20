package az.less.mobile.presentation.merchant.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.model.auth.User
import az.less.mobile.domain.model.auth.UserEcoHeroBadge
import az.less.mobile.domain.model.auth.UserStats
import az.less.mobile.domain.model.auth.UserVenue
import az.less.mobile.domain.repository.AccountRepository
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.ContentRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.presentation.merchant.more.model.MerchCellId
import az.less.mobile.presentation.merchant.more.model.MerchMoreCellModel
import az.less.mobile.presentation.merchant.more.model.MerchMoreCellType
import az.less.mobile.presentation.merchant.more.model.MerchMoreSection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
import lessmobile.composeapp.generated.resources.more_switch_to_client
import lessmobile.composeapp.generated.resources.more_terms_of_service
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class MerchMoreViewModel(
    private val sessionLocalRepository: SessionLocalRepository,
    private val authorizationRepository: AuthorizationRepository,
    private val contentRepository: ContentRepository,
    private val accountRepository: AccountRepository
) : ViewModel(), ContainerHost<MerchMoreState, MerchMoreSideEffect> {

    override val container: Container<MerchMoreState, MerchMoreSideEffect> =
        viewModelScope.container(MerchMoreState())

    init {
        observeUserState()
        refreshUserInBackground()
    }

    private fun refreshUserInBackground() {
        viewModelScope.launch {
            accountRepository.getProfile()
                .onSuccess { profileData ->
                    val userData = profileData.user
                    val updatedUser = User(
                        id = userData.id,
                        name = userData.name,
                        email = userData.email,
                        roles = userData.roles,
                        status = userData.status,
                        avatarUrl = userData.avatar,
                        phone = userData.phone,
                        gender = userData.gender,
                        birthDay = userData.birthDay,
                        emailVerified = userData.emailVerified,
                        currentLocation = userData.currentLocation,
                        venue = userData.venue?.let {
                            UserVenue(
                                id = it.id,
                                name = it.name,
                                businessName = it.businessName,
                                businessAddress = it.businessAddress,
                                businessDescription = it.businessDescription,
                                businessLogo = it.businessLogo,
                                coverImage = it.coverImage,
                                rating = it.rating,
                                totalReviews = it.totalReviews,
                                status = it.status
                            )
                        },
                        stats = profileData.stats?.let {
                            UserStats(
                                mealsSaved = it.mealsSaved,
                                co2Saved = it.co2Saved,
                                moneySaved = it.moneySaved
                            )
                        },
                        ecoHeroBadge = profileData.ecoHeroBadge?.let {
                            UserEcoHeroBadge(
                                level = it.level,
                                message = it.message,
                                mealsSaved = it.mealsSaved,
                                icon = it.icon,
                                color = it.color
                            )
                        }
                    )
                    sessionLocalRepository.updateUser(updatedUser)
                }
        }
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
                                rating = user.venue?.rating?.toString() ?: "0.0",
                                reviewCount = user.venue?.totalReviews?.toString() ?: "0",
                                isPartner = user.isPartner,
                                canSwitchMode = user.canSwitchMode(),
                                sections = buildSections(
                                    notificationEnabled = state.notificationEnabled,
                                    darkModeEnabled = state.darkModeEnabled,
                                    isPartner = user.isPartner,
                                    canSwitchMode = user.canSwitchMode()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

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
                reduce { state.copy(showContactUsBottomSheet = true) }
            }
            MerchCellId.TermsOfService -> fetchTermsAndShow()
            MerchCellId.Notification -> { }
            MerchCellId.DarkMode -> { }
            MerchCellId.SwitchToClient -> postSideEffect(MerchMoreSideEffect.NavigateToClientFlow)
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
                postSideEffect(MerchMoreSideEffect.ShowError(error.message))
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
                sections = buildSections(newEnabled, state.darkModeEnabled, isPartner = state.isPartner, canSwitchMode = state.canSwitchMode)
            )
        }
    }

    private fun handleDarkModeToggleClick() = intent {
        val newEnabled = !state.darkModeEnabled
        reduce {
            state.copy(
                darkModeEnabled = newEnabled,
                sections = buildSections(state.notificationEnabled, newEnabled, isPartner = state.isPartner, canSwitchMode = state.canSwitchMode)
            )
        }
    }

    private fun handleLogoutClicked() = intent {
        reduce { state.copy(isLoading = true) }

        val refreshToken = sessionLocalRepository.getRefreshToken()
        if (refreshToken != null) {
            authorizationRepository.logout(refreshToken)
        }

        sessionLocalRepository.clearSession()
        postSideEffect(MerchMoreSideEffect.Logout)
    }

    private fun buildSections(
        notificationEnabled: Boolean,
        darkModeEnabled: Boolean,
        isPartner: Boolean,
        canSwitchMode: Boolean
    ): List<MerchMoreSection> {
        val appCells = mutableListOf<MerchMoreCellModel>()

        if (isPartner) {
            appCells.add(
                MerchMoreCellModel(
                    id = MerchCellId.Places,
                    titleRes = Res.string.merch_more_places,
                    icon = Res.drawable.ic_explore_24dp,
                    type = MerchMoreCellType.Navigation
                )
            )
        }

        appCells.add(
            MerchMoreCellModel(
                id = MerchCellId.Notification,
                titleRes = Res.string.more_notification,
                icon = Res.drawable.ic_notification_24dp,
                type = MerchMoreCellType.Toggle(notificationEnabled)
            )
        )

        appCells.add(
            MerchMoreCellModel(
                id = MerchCellId.DarkMode,
                titleRes = Res.string.merch_more_dark_mode,
                icon = Res.drawable.ic_more_24dp,
                type = MerchMoreCellType.Toggle(darkModeEnabled),
                showDivider = canSwitchMode
            )
        )

        if (canSwitchMode) {
            appCells.add(
                MerchMoreCellModel(
                    id = MerchCellId.SwitchToClient,
                    titleRes = Res.string.more_switch_to_client,
                    icon = Res.drawable.ic_explore_24dp,
                    type = MerchMoreCellType.Navigation,
                    showDivider = false
                )
            )
        }

        return listOf(
            MerchMoreSection(
                titleRes = Res.string.more_section_application,
                cells = appCells
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
