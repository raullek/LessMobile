package az.less.mobile.presentation.client.main.voucher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsToolBar
import az.less.mobile.presentation.client.main.voucher.components.VoucherCard
import az.less.mobile.presentation.client.main.voucher.components.VoucherEmptyState
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.voucher_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun VoucherScreen(
    viewModel: VoucherViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is VoucherSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is VoucherSideEffect.ShowError -> {}
            is VoucherSideEffect.ShowCopiedToast -> {
                clipboardManager.setText(AnnotatedString(sideEffect.message))
            }
        }
    }

    VoucherScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

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
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LessTheme.colors.textIconsBrand)
                }
            }
            state.vouchers.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = LessTheme.spacing.medium),
                    contentAlignment = Alignment.Center
                ) {
                    VoucherEmptyState()
                }
            }
            else -> {
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
    }
}
