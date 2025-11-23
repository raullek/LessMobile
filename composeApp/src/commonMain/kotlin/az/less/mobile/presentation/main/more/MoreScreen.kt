package az.less.mobile.presentation.main.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
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
import az.less.designsystem.components.DsSectionHeader
import az.less.mobile.presentation.main.more.components.MoreHeader
import az.less.mobile.presentation.main.more.models.MoreCellType
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_account_24dp
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
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
    navController: NavController
) {
    val state by viewModel.collectAsState()
    
    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MoreSideEffect.NavigateToLogin -> {
                // Handle login navigation
                // navController.navigate("login")
            }
            is MoreSideEffect.NavigateToAccount -> {
                // navController.navigate("account")
            }
            is MoreSideEffect.NavigateToPaymentMethods -> {
                // navController.navigate("payment_methods")
            }
            is MoreSideEffect.NavigateToVoucher -> {
                // navController.navigate("voucher")
            }
            is MoreSideEffect.NavigateToHistory -> {
                // navController.navigate("history")
            }
            is MoreSideEffect.NavigateToSettings -> {
                // navController.navigate("settings")
            }
            is MoreSideEffect.NavigateToContactUs -> {
                // navController.navigate("contact_us")
            }
            is MoreSideEffect.NavigateToSignStore -> {
                // navController.navigate("sign_store")
            }
            is MoreSideEffect.NavigateToTermsOfService -> {
                // navController.navigate("terms")
            }
            is MoreSideEffect.NavigateToHowToUse -> {
                // navController.navigate("how_to_use")
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
        state.sections.forEach { section ->
            // Section header
            item(key = "header_${section.title}") {
                DsSectionHeader(title = section.title)
            }
            
            // Section cells
            items(
                items = section.cells,
                key = { cell -> cell.id }
            ) { cell ->
                DsCell(
                    title = cell.title,
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
            item(key = "spacing_${section.title}") {
                Spacer(modifier = Modifier.height(LessTheme.spacing.large).background(LessTheme.colors.backgroundPrimary))
            }
        }
        
        // Logout button (only for authenticated users)
        if (state.isLoggedIn) {
            item {
                DsButton(
                    text = "Logout",
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
            Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        }
    }
}
