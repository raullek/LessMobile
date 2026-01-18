package az.less.mobile.presentation.client.main.voucher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsToolBar
import az.less.mobile.presentation.client.main.voucher.components.VoucherCard
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_copy_24dp
import lessmobile.composeapp.generated.resources.voucher_title
import lessmobile.composeapp.generated.resources.action_share
import lessmobile.composeapp.generated.resources.action_copy
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful VoucherScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun VoucherScreen(
    viewModel: VoucherViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is VoucherSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is VoucherSideEffect.ShowError -> {
                // Show error snackbar
            }
            is VoucherSideEffect.ShowCopiedToast -> {
                // Show toast message
            }
            is VoucherSideEffect.ShareReferralCode -> {
                // Trigger share intent
            }
        }
    }

    VoucherScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless VoucherScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun VoucherScreenContent(
    state: VoucherState,
    onIntent: (VoucherIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LessTheme.colors.backgroundSecond,
        topBar = {
            DsToolBar(
                title = stringResource(Res.string.voucher_title),
                onBackClick = { onIntent(VoucherIntent.OnBackClicked) },
                backgroundColor = LessTheme.colors.backgroundSecond
            )
        },
        bottomBar = {
            // Bottom section with referral code and share button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LessTheme.colors.backgroundSecond)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = LessTheme.spacing.medium)
                    .padding(bottom = LessTheme.spacing.medium)
            ) {
                // Referral code input with copy button - using secondary element color and taller height
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(LessTheme.radius.medium))
                        .background(LessTheme.colors.backgroundPrimary)
                        .padding(horizontal = LessTheme.spacing.medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.referralCode,
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsGrey
                    )
                    Icon(
                        painter = painterResource(Res.drawable.ic_copy_24dp),
                        contentDescription = stringResource(Res.string.action_copy),
                        tint = LessTheme.colors.textIconsGrey,
                        modifier = Modifier
                            .size(LessTheme.size.medium)
                            .clickable { onIntent(VoucherIntent.OnCopyCodeClicked(state.referralCode)) }
                    )
                }

                Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

                // Share button
                DsButton(
                    text = stringResource(Res.string.action_share),
                    onClick = { onIntent(VoucherIntent.OnShareClicked) },
                    variant = ButtonVariant.Primary,
                    size = ButtonSize.Large,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = LessTheme.spacing.medium,
                vertical = LessTheme.spacing.medium
            ),
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
        ) {
            items(
                items = state.vouchers,
                key = { it.id }
            ) { voucher ->
                VoucherCard(voucher = voucher)
            }
        }
    }
}
