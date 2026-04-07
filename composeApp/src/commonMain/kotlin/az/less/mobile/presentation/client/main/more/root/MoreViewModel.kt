package az.less.mobile.presentation.client.main.more.root

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
import az.less.mobile.presentation.theme.ThemeManager
import az.less.mobile.presentation.client.main.more.root.models.CellId
import dev.jordond.compass.permissions.LocationPermissionController
import dev.jordond.compass.permissions.mobile
import az.less.mobile.presentation.client.main.more.root.models.MoreCellModel
import az.less.mobile.presentation.client.main.more.root.models.MoreCellType
import az.less.mobile.presentation.client.main.more.root.models.MoreSection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_map_24dp
import lessmobile.composeapp.generated.resources.ic_account_24dp
import lessmobile.composeapp.generated.resources.ic_bubble_question_24dp
import lessmobile.composeapp.generated.resources.ic_customer_support_24dp
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_notification_24dp
import lessmobile.composeapp.generated.resources.ic_payment_card_24dp
import lessmobile.composeapp.generated.resources.ic_terms_file_24dp
import lessmobile.composeapp.generated.resources.ic_voucher_24dp
import lessmobile.composeapp.generated.resources.more_account
import lessmobile.composeapp.generated.resources.more_contact_us
import lessmobile.composeapp.generated.resources.more_how_to_use
import lessmobile.composeapp.generated.resources.more_location
import lessmobile.composeapp.generated.resources.more_location_granted
import lessmobile.composeapp.generated.resources.more_location_not_granted
import lessmobile.composeapp.generated.resources.ic_more_24dp
import lessmobile.composeapp.generated.resources.merch_more_dark_mode
import lessmobile.composeapp.generated.resources.more_notification
import lessmobile.composeapp.generated.resources.more_payment_methods
import lessmobile.composeapp.generated.resources.more_section_application
import lessmobile.composeapp.generated.resources.more_section_support
import lessmobile.composeapp.generated.resources.more_terms_of_service
import lessmobile.composeapp.generated.resources.more_switch_to_merchant
import lessmobile.composeapp.generated.resources.more_vouchers
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for More Screen using Orbit MVI
 */
class MoreViewModel(
    private val userLocalRepository: SessionLocalRepository,
    private val authorizationRepository: AuthorizationRepository,
    private val contentRepository: ContentRepository,
    private val accountRepository: AccountRepository,
    private val themeManager: ThemeManager
) : ViewModel(), ContainerHost<MoreState, MoreSideEffect> {

    override val container: Container<MoreState, MoreSideEffect> =
        viewModelScope.container(MoreState())

    private val permissionController: LocationPermissionController = LocationPermissionController.mobile()

    init {
        logUserInfo()
        checkLocationPermission()
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
                            sections = if (state.isLoggedIn) {
                                buildAuthSections(state.notificationEnabled, isDark, state.locationPermissionGranted, state.canSwitchMode)
                            } else {
                                buildNonAuthSections(state.notificationEnabled, isDark, state.locationPermissionGranted)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun logUserInfo() {
        viewModelScope.launch {
            val user = userLocalRepository.currentUser.first()
            println("[MoreViewModel] === User Info from DataStore ===")
            println("[MoreViewModel] user: $user")
            println("[MoreViewModel] accessToken: ${userLocalRepository.getAccessToken()}")
            println("[MoreViewModel] refreshToken: ${userLocalRepository.getRefreshToken()}")
            println("[MoreViewModel] ================================")
        }
    }

    private fun refreshUserInBackground() {
        viewModelScope.launch {
            // Only refresh if user is logged in
            val token = userLocalRepository.getAccessToken() ?: return@launch

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
                    userLocalRepository.updateUser(updatedUser)

                    // Update eco stats in UI state
                    intent {
                        reduce {
                            state.copy(
                                co2Saved = updatedUser.stats?.co2Saved?.toString(),
                                moneySaved = updatedUser.stats?.moneySaved?.toString(),
                                ecoHeroTitle = updatedUser.ecoHeroBadge?.level,
                                ecoHeroDescription = updatedUser.ecoHeroBadge?.message
                            )
                        }
                    }
                }
        }
    }

    private fun checkLocationPermission() {
        viewModelScope.launch {
            val hasPermission = permissionController.hasPermission()
            intent {
                reduce {
                    state.copy(
                        locationPermissionGranted = hasPermission,
                        sections = if (state.isLoggedIn) {
                            buildAuthSections(state.notificationEnabled, state.darkModeEnabled, hasPermission, state.canSwitchMode)
                        } else {
                            buildNonAuthSections(state.notificationEnabled, state.darkModeEnabled, hasPermission)
                        }
                    )
                }
            }
        }
    }

    private fun observeUserState() {
        viewModelScope.launch {
            userLocalRepository.currentUser.collectLatest { user ->
                intent {
                    if (user != null) {
                        val canSwitch = user.canSwitchMode()
                        reduce {
                            state.copy(
                                isLoggedIn = true,
                                canSwitchMode = canSwitch,
                                userName = user.name,
                                userEmail = user.email,
                                userAvatarUrl = user.avatarUrl,
                                sections = buildAuthSections(state.notificationEnabled, state.darkModeEnabled, state.locationPermissionGranted, canSwitch)
                            )
                        }
                    } else {
                        reduce {
                            state.copy(
                                isLoggedIn = false,
                                userName = null,
                                userEmail = null,
                                userAvatarUrl = null,
                                sections = buildNonAuthSections(state.notificationEnabled, state.darkModeEnabled, state.locationPermissionGranted)
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
            is MoreIntent.OnDarkModeToggleClick -> handleDarkModeToggleClick()
            is MoreIntent.OnContactUsDismiss -> handleContactUsDismiss()
            is MoreIntent.OnContactUsItemClick -> handleContactUsItemClick(intent.itemId)
            is MoreIntent.OnTermsDismiss -> handleTermsDismiss()
        }
    }

    private fun handleLoginClicked() = intent {
        postSideEffect(MoreSideEffect.NavigateToLogin)
    }

    private fun handleLogoutClicked() = intent {
        reduce { state.copy(isLoading = true) }

        val refreshToken = userLocalRepository.getRefreshToken()
        if (refreshToken != null) {
            authorizationRepository.logout(refreshToken)
        }

        userLocalRepository.clearSession()
    }

    private fun handleCellClick(cellId: CellId) = intent {
        when (cellId) {
            CellId.Account -> postSideEffect(MoreSideEffect.NavigateToAccount)
            CellId.PaymentMethods -> postSideEffect(MoreSideEffect.NavigateToPaymentMethods)
            CellId.Voucher -> postSideEffect(MoreSideEffect.NavigateToVoucher)
            CellId.Settings -> postSideEffect(MoreSideEffect.NavigateToSettings)
            CellId.ContactUs -> {
                reduce {
                    state.copy(showContactUsBottomSheet = true)
                }
            }
            CellId.SignStore -> postSideEffect(MoreSideEffect.NavigateToSignStore)
            CellId.TermsOfService -> fetchTermsAndShow()
            CellId.HowToUse -> postSideEffect(MoreSideEffect.NavigateToHowToUse)
            CellId.Location -> postSideEffect(MoreSideEffect.NavigateToAppSettings)
            CellId.Notification -> {
                // Notification is handled separately via toggle
            }
            CellId.DarkMode -> {
                // Dark mode is handled separately via toggle
            }
            CellId.SwitchToMerchant -> postSideEffect(MoreSideEffect.NavigateToMerchantFlow)
        }
    }

    private fun handleContactUsDismiss() = intent {
        reduce {
            state.copy(showContactUsBottomSheet = false)
        }
    }

    private fun handleContactUsItemClick(itemId: String) = intent {
        reduce {
            state.copy(showContactUsBottomSheet = false)
        }
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
                postSideEffect(MoreSideEffect.ShowError(error.message))
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
                    buildAuthSections(newEnabled, state.darkModeEnabled, state.locationPermissionGranted, state.canSwitchMode)
                } else {
                    buildNonAuthSections(newEnabled, state.darkModeEnabled, state.locationPermissionGranted)
                }
            )
        }
    }

    private fun handleDarkModeToggleClick() {
        themeManager.toggleDarkMode(viewModelScope)
    }

    /**
     * Build sections for authenticated users
     */
    private fun buildAuthSections(notificationEnabled: Boolean, darkModeEnabled: Boolean, locationGranted: Boolean, canSwitchMode: Boolean): List<MoreSection> {
        val appCells = mutableListOf(
            MoreCellModel(
                id = CellId.Account,
                titleRes = Res.string.more_account,
                icon = Res.drawable.ic_account_24dp,
                type = MoreCellType.Navigation
            ),
            MoreCellModel(
                id = CellId.PaymentMethods,
                titleRes = Res.string.more_payment_methods,
                icon = Res.drawable.ic_payment_card_24dp,
                type = MoreCellType.Navigation
            ),
            MoreCellModel(
                id = CellId.Voucher,
                titleRes = Res.string.more_vouchers,
                icon = Res.drawable.ic_voucher_24dp,
                type = MoreCellType.Navigation
            ),
            MoreCellModel(
                id = CellId.Location,
                titleRes = Res.string.more_location,
                subtitleRes = if (locationGranted) Res.string.more_location_granted else Res.string.more_location_not_granted,
                icon = Res.drawable.ic_map_24dp,
                type = MoreCellType.Navigation
            ),
            MoreCellModel(
                id = CellId.Notification,
                titleRes = Res.string.more_notification,
                icon = Res.drawable.ic_notification_24dp,
                type = MoreCellType.Toggle(notificationEnabled),
            ),
            MoreCellModel(
                id = CellId.DarkMode,
                titleRes = Res.string.merch_more_dark_mode,
                icon = Res.drawable.ic_more_24dp,
                type = MoreCellType.Toggle(darkModeEnabled),
                showDivider = canSwitchMode
            )
        )

        if (canSwitchMode) {
            appCells.add(
                MoreCellModel(
                    id = CellId.SwitchToMerchant,
                    titleRes = Res.string.more_switch_to_merchant,
                    icon = Res.drawable.ic_explore_24dp,
                    type = MoreCellType.Navigation,
                    showDivider = false
                )
            )
        }

        return listOf(
            MoreSection(
                titleRes = Res.string.more_section_application,
                cells = appCells
            ),
            MoreSection(
                titleRes = Res.string.more_section_support,
                cells = listOf(
                    MoreCellModel(
                        id = CellId.ContactUs,
                        titleRes = Res.string.more_contact_us,
                        icon = Res.drawable.ic_customer_support_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.TermsOfService,
                        titleRes = Res.string.more_terms_of_service,
                        icon = Res.drawable.ic_account_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.HowToUse,
                        titleRes = Res.string.more_how_to_use,
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
    private fun buildNonAuthSections(notificationEnabled: Boolean, darkModeEnabled: Boolean, locationGranted: Boolean): List<MoreSection> {
        return listOf(
            MoreSection(
                titleRes = Res.string.more_section_application,
                cells = listOf(
                    MoreCellModel(
                        id = CellId.Location,
                        titleRes = Res.string.more_location,
                        subtitleRes = if (locationGranted) Res.string.more_location_granted else Res.string.more_location_not_granted,
                        icon = Res.drawable.ic_map_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.Notification,
                        titleRes = Res.string.more_notification,
                        icon = Res.drawable.ic_notification_24dp,
                        type = MoreCellType.Toggle(notificationEnabled),
                    ),
                    MoreCellModel(
                        id = CellId.DarkMode,
                        titleRes = Res.string.merch_more_dark_mode,
                        icon = Res.drawable.ic_more_24dp,
                        type = MoreCellType.Toggle(darkModeEnabled),
                        showDivider = false
                    )
                )
            ),
            MoreSection(
                titleRes = Res.string.more_section_support,
                cells = listOf(
                    MoreCellModel(
                        id = CellId.ContactUs,
                        titleRes = Res.string.more_contact_us,
                        icon = Res.drawable.ic_account_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.TermsOfService,
                        titleRes = Res.string.more_terms_of_service,
                        icon = Res.drawable.ic_terms_file_24dp,
                        type = MoreCellType.Navigation
                    ),
                    MoreCellModel(
                        id = CellId.HowToUse,
                        titleRes = Res.string.more_how_to_use,
                        icon = Res.drawable.ic_bubble_question_24dp,
                        type = MoreCellType.Navigation,
                        showDivider = false
                    )
                )
            )
        )
    }
}
