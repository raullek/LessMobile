package az.less.mobile.presentation.client.main.more.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.presentation.client.main.more.root.models.CellId
import dev.jordond.compass.permissions.LocationPermissionController
import dev.jordond.compass.permissions.mobile
import az.less.mobile.presentation.client.main.more.root.models.MoreCellModel
import az.less.mobile.presentation.client.main.more.root.models.MoreCellType
import az.less.mobile.presentation.client.main.more.root.models.MoreSection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_map_24dp
import lessmobile.composeapp.generated.resources.ic_account_24dp
import lessmobile.composeapp.generated.resources.ic_bubble_question_24dp
import lessmobile.composeapp.generated.resources.ic_clock_24dp
import lessmobile.composeapp.generated.resources.ic_customer_support_24dp
import lessmobile.composeapp.generated.resources.ic_notification_24dp
import lessmobile.composeapp.generated.resources.ic_payment_card_24dp
import lessmobile.composeapp.generated.resources.ic_terms_file_24dp
import lessmobile.composeapp.generated.resources.ic_voucher_24dp
import lessmobile.composeapp.generated.resources.more_account
import lessmobile.composeapp.generated.resources.more_contact_us
import lessmobile.composeapp.generated.resources.more_history
import lessmobile.composeapp.generated.resources.more_how_to_use
import lessmobile.composeapp.generated.resources.more_location
import lessmobile.composeapp.generated.resources.more_location_granted
import lessmobile.composeapp.generated.resources.more_location_not_granted
import lessmobile.composeapp.generated.resources.more_notification
import lessmobile.composeapp.generated.resources.more_payment_methods
import lessmobile.composeapp.generated.resources.more_section_application
import lessmobile.composeapp.generated.resources.more_section_support
import lessmobile.composeapp.generated.resources.more_terms_of_service
import lessmobile.composeapp.generated.resources.more_vouchers
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

    private val permissionController: LocationPermissionController = LocationPermissionController.mobile()

    init {
        checkLocationPermission()
        observeUserState()
    }

    private fun checkLocationPermission() {
        viewModelScope.launch {
            val hasPermission = permissionController.hasPermission()
            intent {
                reduce {
                    state.copy(
                        locationPermissionGranted = hasPermission,
                        sections = if (state.isLoggedIn) {
                            buildAuthSections(state.notificationEnabled, hasPermission)
                        } else {
                            buildNonAuthSections(state.notificationEnabled, hasPermission)
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
                        reduce {
                            state.copy(
                                isLoggedIn = true,
                                userName = user.name,
                                userEmail = user.email,
                                userAvatarUrl = user.avatarUrl,
                                sections = buildAuthSections(state.notificationEnabled, state.locationPermissionGranted)
                            )
                        }
                    } else {
                        reduce {
                            state.copy(
                                isLoggedIn = false,
                                userName = null,
                                userEmail = null,
                                userAvatarUrl = null,
                                sections = buildNonAuthSections(state.notificationEnabled, state.locationPermissionGranted)
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
            CellId.Location -> postSideEffect(MoreSideEffect.NavigateToAppSettings)
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
                    buildAuthSections(newEnabled, state.locationPermissionGranted)
                } else {
                    buildNonAuthSections(newEnabled, state.locationPermissionGranted)
                }
            )
        }
    }

    /**
     * Build sections for authenticated users
     */
    private fun buildAuthSections(notificationEnabled: Boolean, locationGranted: Boolean): List<MoreSection> {
        return listOf(
            MoreSection(
                titleRes = Res.string.more_section_application,
                cells = listOf(
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
                        id = CellId.History,
                        titleRes = Res.string.more_history,
                        icon = Res.drawable.ic_clock_24dp,
                        type = MoreCellType.Navigation,
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
    private fun buildNonAuthSections(notificationEnabled: Boolean, locationGranted: Boolean): List<MoreSection> {
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
