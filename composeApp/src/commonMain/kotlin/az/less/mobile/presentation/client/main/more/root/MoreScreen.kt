package az.less.mobile.presentation.client.main.more.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.CellLeadingContent
import az.less.designsystem.components.CellType
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsCell
import az.less.designsystem.components.DsListBottomSheet
import az.less.designsystem.components.DsSectionHeader
import az.less.designsystem.components.DsTextBottomSheet
import az.less.designsystem.components.ListBottomSheetItem
import az.less.mobile.navigation.ClientRoute
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import org.jetbrains.compose.resources.painterResource
import az.less.mobile.presentation.client.main.more.root.components.MoreHeader
import az.less.mobile.presentation.client.main.more.root.models.MoreCellType

import az.less.mobile.utils.openAppSettings
import lessmobile.composeapp.generated.resources.more_contact_us
import lessmobile.composeapp.generated.resources.contact_instagram
import lessmobile.composeapp.generated.resources.contact_tiktok
import lessmobile.composeapp.generated.resources.contact_facebook
import lessmobile.composeapp.generated.resources.contact_telegram
import lessmobile.composeapp.generated.resources.contact_whatsapp
import lessmobile.composeapp.generated.resources.terms_title
import lessmobile.composeapp.generated.resources.action_logout
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful MoreScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun MoreScreen(
    viewModel: MoreViewModel = koinViewModel(),
    navController: NavController,
    navigateToMerchant: () -> Unit
) {
    val state by viewModel.collectAsState()
    
    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MoreSideEffect.NavigateToLogin -> {
                navController.navigate(ClientRoute.Welcome)
            }
            is MoreSideEffect.NavigateToAccount -> {
                navController.navigate(ClientRoute.Account)
            }
            is MoreSideEffect.NavigateToPaymentMethods -> {
                navController.navigate(ClientRoute.PaymentMethods)
            }
            is MoreSideEffect.NavigateToVoucher -> {
                navController.navigate(ClientRoute.Voucher)
            }
            is MoreSideEffect.NavigateToHistory -> {
                navigateToMerchant.invoke()
            }
            is MoreSideEffect.NavigateToSettings -> {
                // navController.navigate("settings")
            }
            is MoreSideEffect.NavigateToContactUs -> {
                // Handled via state (showContactUsBottomSheet)
            }
            is MoreSideEffect.NavigateToSignStore -> {
                // navController.navigate("sign_store")
            }
            is MoreSideEffect.NavigateToTermsOfService -> {
                // Handled via state (showTermsBottomSheet)
            }
            is MoreSideEffect.NavigateToHowToUse -> {
                navController.navigate(ClientRoute.Welcome)
            }
            is MoreSideEffect.NavigateToAppSettings -> {
                openAppSettings()
            }
            is MoreSideEffect.ShowError -> {
                // Show error snackbar or dialog
            }
        }
    }
    
    // Render the stateless UI
    MoreScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
    
    // Contact Us Bottom Sheet
    if (state.showContactUsBottomSheet) {
        val contactItems = listOf(
            ListBottomSheetItem(
                id = "instagram",
                title = stringResource(Res.string.contact_instagram),
                icon = null // TODO: Add Instagram icon
            ),
            ListBottomSheetItem(
                id = "tiktok",
                title = stringResource(Res.string.contact_tiktok),
                icon = null // TODO: Add TikTok icon
            ),
            ListBottomSheetItem(
                id = "facebook",
                title = stringResource(Res.string.contact_facebook),
                icon = null // TODO: Add Facebook icon
            ),
            ListBottomSheetItem(
                id = "telegram",
                title = stringResource(Res.string.contact_telegram),
                icon = null // TODO: Add Telegram icon
            ),
            ListBottomSheetItem(
                id = "whatsapp",
                title = stringResource(Res.string.contact_whatsapp),
                icon = null // TODO: Add Whatsapp icon
            )
        )

        DsListBottomSheet(
            title = stringResource(Res.string.more_contact_us),
            items = contactItems,
            onItemClick = { itemId ->
                viewModel.onIntent(MoreIntent.OnContactUsItemClick(itemId))
            },
            onDismiss = {
                viewModel.onIntent(MoreIntent.OnContactUsDismiss)
            },
            chevronIcon = painterResource(Res.drawable.ic_chevron_right_24dp)
        )
    }
    
    // Terms & Conditions Bottom Sheet
    if (state.showTermsBottomSheet) {
        val termsContent = """
            1 Header
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
            
            1.1 Header
            Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            
            Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.
            
            Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.
        """.trimIndent()
        
        DsTextBottomSheet(
            title = stringResource(Res.string.terms_title),
            content = termsContent,
            onDismiss = {
                viewModel.onIntent(MoreIntent.OnTermsDismiss)
            }
        )
    }
}

/**
 * Stateless MoreScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun MoreScreenContent(
    state: MoreState,
    onIntent: (MoreIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = LessTheme.size.large) // Space for bottom nav bar
        ,
    ) {
        // Top spacing
        item {
            Spacer(modifier = Modifier.height(LessTheme.spacing.small))
        }
        
        // Header with two states: logged in / not logged in
        item {
            MoreHeader(
                isLoggedIn = state.isLoggedIn,
                onLoginClick = { onIntent(MoreIntent.OnLoginClicked) },
                userName = state.userName,
                userEmail = state.userEmail,
                userAvatarUrl = state.userAvatarUrl,
                co2Saved = state.co2Saved,
                moneySaved = state.moneySaved,
                ecoHeroTitle = state.ecoHeroTitle,
                ecoHeroDescription = state.ecoHeroDescription,
                modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))
        }
        
        // Sections
        state.sections.forEachIndexed { index, section ->
            // Section header
            item(key = "header_$index") {
                DsSectionHeader(title = stringResource(section.titleRes))
            }

            // Section cells
            items(
                items = section.cells,
                key = { cell -> cell.id }
            ) { cell ->
                DsCell(
                    title = stringResource(cell.titleRes),
                    subtitle = cell.subtitleRes?.let { stringResource(it) },
                    leadingContent = cell.icon?.let { CellLeadingContent(icon = it) },
                    type = when (cell.type) {
                        is MoreCellType.Navigation -> CellType.Navigation(
                            onClick = { onIntent(MoreIntent.OnCellClick(cell.id)) },
                            trailingIcon = Res.drawable.ic_chevron_right_24dp
                        )
                        is MoreCellType.Toggle -> CellType.Toggle(
                            checked = cell.type.checked,
                            onCheckedChange = { onIntent(MoreIntent.OnNotificationToggleClick) }
                        )
                    },
                    showDivider = cell.showDivider,
                    modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                )
            }

            // Spacing after section
            item(key = "spacing_$index") {
                Spacer(modifier = Modifier.height(LessTheme.spacing.large).background(LessTheme.colors.backgroundPrimary))
            }
        }
        
        // Logout button (only for authenticated users)
        if (state.isLoggedIn) {
            item {
                DsButton(
                    text = stringResource(Res.string.action_logout),
                    onClick = { onIntent(MoreIntent.OnLogoutClicked) },
                    variant = ButtonVariant.Secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = LessTheme.spacing.medium)
                )
            }
        }
        
        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))
        }
    }
}
