package az.less.mobile.presentation.merchant.more

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
import az.less.mobile.navigation.MerchantScreens
import az.less.mobile.presentation.merchant.more.components.MerchMoreHeader
import az.less.mobile.presentation.merchant.more.model.MerchCellId
import az.less.mobile.presentation.merchant.more.model.MerchMoreCellType
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful MerchMoreScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun MerchMoreScreen(
    viewModel: MerchMoreViewModel = koinViewModel(),
    navController: NavController,
    navigateToClientFlow: () -> Unit
) {
    val state by viewModel.collectAsState()

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MerchMoreSideEffect.NavigateToPlaces -> {
                navController.navigate(MerchantScreens.Places.route)
            }
            is MerchMoreSideEffect.Logout -> {
                // TODO: Handle logout - clear session and navigate to login
                navigateToClientFlow.invoke()
            }
            is MerchMoreSideEffect.ShowError -> {
                // Show error snackbar or dialog
            }
        }
    }

    // Render the stateless UI
    MerchMoreScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )

    // Contact Us Bottom Sheet
    if (state.showContactUsBottomSheet) {
        val contactItems = listOf(
            ListBottomSheetItem(
                id = "instagram",
                title = "Instagram",
                icon = null
            ),
            ListBottomSheetItem(
                id = "tiktok",
                title = "TikTok",
                icon = null
            ),
            ListBottomSheetItem(
                id = "facebook",
                title = "Facebook",
                icon = null
            ),
            ListBottomSheetItem(
                id = "telegram",
                title = "Telegram",
                icon = null
            ),
            ListBottomSheetItem(
                id = "whatsapp",
                title = "Whatsapp",
                icon = null
            )
        )

        DsListBottomSheet(
            title = "Contact us",
            items = contactItems,
            onItemClick = { itemId ->
                viewModel.onIntent(MerchMoreIntent.OnContactUsItemClick(itemId))
            },
            onDismiss = {
                viewModel.onIntent(MerchMoreIntent.OnContactUsDismiss)
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
            title = "Terms&Conditions",
            content = termsContent,
            onDismiss = {
                viewModel.onIntent(MerchMoreIntent.OnTermsDismiss)
            }
        )
    }
}

/**
 * Stateless MerchMoreScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun MerchMoreScreenContent(
    state: MerchMoreState,
    onIntent: (MerchMoreIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundPrimary)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = LessTheme.size.large)
    ) {
        // Header with merchant info
        item {
            MerchMoreHeader(
                merchantName = state.merchantName,
                merchantEmail = state.merchantEmail,
                rating = state.rating,
                reviewCount = state.reviewCount
            )
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
                        is MerchMoreCellType.Navigation -> CellType.Navigation(
                            onClick = { onIntent(MerchMoreIntent.OnCellClick(cell.id)) },
                            trailingIcon = Res.drawable.ic_chevron_right_24dp
                        )
                        is MerchMoreCellType.Toggle -> CellType.Toggle(
                            checked = cell.type.checked,
                            onCheckedChange = {
                                when (cell.id) {
                                    MerchCellId.Notification -> onIntent(MerchMoreIntent.OnNotificationToggleClick)
                                    MerchCellId.DarkMode -> onIntent(MerchMoreIntent.OnDarkModeToggleClick)
                                    else -> {}
                                }
                            }
                        )
                    },
                    showDivider = cell.showDivider,
                    modifier = Modifier.padding(horizontal = LessTheme.spacing.medium)
                )
            }

            // Spacing after section
            item(key = "spacing_${section.title}") {
                Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
            }
        }

        // Logout button
        item {
            DsButton(
                text = "Logout",
                onClick = { onIntent(MerchMoreIntent.OnLogoutClicked) },
                variant = ButtonVariant.Secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LessTheme.spacing.medium)
            )
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
        }
    }
}
