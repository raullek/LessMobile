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
import az.less.mobile.navigation.MerchantRoute
import az.less.mobile.presentation.merchant.more.components.MerchMoreHeader
import az.less.mobile.presentation.merchant.more.model.MerchCellId
import az.less.mobile.presentation.merchant.more.model.MerchMoreCellType
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.action_logout
import lessmobile.composeapp.generated.resources.contact_facebook
import lessmobile.composeapp.generated.resources.contact_instagram
import lessmobile.composeapp.generated.resources.contact_telegram
import lessmobile.composeapp.generated.resources.contact_tiktok
import lessmobile.composeapp.generated.resources.contact_whatsapp
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import lessmobile.composeapp.generated.resources.more_contact_us
import lessmobile.composeapp.generated.resources.terms_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
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
                navController.navigate(MerchantRoute.Places)
            }
            is MerchMoreSideEffect.NavigateToClientFlow -> {
                navigateToClientFlow.invoke()
            }
            is MerchMoreSideEffect.Logout -> {
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
                title = stringResource(Res.string.contact_instagram),
                icon = null
            ),
            ListBottomSheetItem(
                id = "tiktok",
                title = stringResource(Res.string.contact_tiktok),
                icon = null
            ),
            ListBottomSheetItem(
                id = "facebook",
                title = stringResource(Res.string.contact_facebook),
                icon = null
            ),
            ListBottomSheetItem(
                id = "telegram",
                title = stringResource(Res.string.contact_telegram),
                icon = null
            ),
            ListBottomSheetItem(
                id = "whatsapp",
                title = stringResource(Res.string.contact_whatsapp),
                icon = null
            )
        )

        DsListBottomSheet(
            title = stringResource(Res.string.more_contact_us),
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
        val displayContent = if (state.isTermsLoading) {
            ""
        } else {
            if (state.isTermsHtml) {
                stripHtmlTags(state.termsContent.orEmpty())
            } else {
                state.termsContent.orEmpty()
            }
        }

        DsTextBottomSheet(
            title = state.termsTitle ?: stringResource(Res.string.terms_title),
            content = displayContent,
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
            item(key = "header_${section.titleRes}") {
                DsSectionHeader(title = stringResource(section.titleRes))
            }

            // Section cells
            items(
                items = section.cells,
                key = { cell -> cell.id }
            ) { cell ->
                DsCell(
                    title = stringResource(cell.titleRes),
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
            item(key = "spacing_${section.titleRes}") {
                Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
            }
        }

        // Logout button
        item {
            DsButton(
                text = stringResource(Res.string.action_logout),
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

private fun stripHtmlTags(html: String): String {
    return html
        .replace(Regex("<br\\s*/?>"), "\n")
        .replace(Regex("</p>"), "\n\n")
        .replace(Regex("</div>"), "\n")
        .replace(Regex("</li>"), "\n")
        .replace(Regex("<li[^>]*>"), "- ")
        .replace(Regex("<[^>]+>"), "")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&nbsp;", " ")
        .replace(Regex("\n{3,}"), "\n\n")
        .trim()
}
