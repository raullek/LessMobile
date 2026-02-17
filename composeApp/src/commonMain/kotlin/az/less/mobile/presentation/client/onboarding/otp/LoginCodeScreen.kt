package az.less.mobile.presentation.client.onboarding.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.AnimatedToast
import az.less.designsystem.components.DsToolBar
import az.less.designsystem.components.ToastType
import az.less.mobile.presentation.client.onboarding.otp.components.OtpCodeInput
import kotlinx.coroutines.delay
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.login_code_title
import lessmobile.composeapp.generated.resources.login_code_subtitle
import lessmobile.composeapp.generated.resources.login_code_default_email
import lessmobile.composeapp.generated.resources.login_code_resend
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

data class ToastState(
    val message: String,
    val type: ToastType
)

@Composable
fun LoginCodeScreen(
    navController: NavController,
    email: String = "",
    viewModel: LoginCodeViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    var toastState by remember { mutableStateOf<ToastState?>(null) }

    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            viewModel.setEmail(email)
        }
    }

    LaunchedEffect(state.isCodeValid) {
        if (state.isCodeValid && !state.isLoading) {
            viewModel.onIntent(LoginCodeIntent.OnVerifyClicked)
        }
    }

    LaunchedEffect(toastState) {
        if (toastState != null) {
            delay(3000)
            toastState = null
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginCodeSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is LoginCodeSideEffect.NavigateNext -> {
                navController.popBackStack("more", inclusive = false)
            }
            is LoginCodeSideEffect.ShowSuccess -> {
                toastState = ToastState(sideEffect.message, ToastType.Success)
            }
            is LoginCodeSideEffect.ShowError -> {
                toastState = ToastState(sideEffect.message, ToastType.Error)
            }
        }
    }

    LoginCodeScreenContent(
        state = state,
        toastState = toastState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun LoginCodeScreenContent(
    state: LoginCodeState,
    toastState: ToastState?,
    onIntent: (LoginCodeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            DsToolBar(
                title = "",
                onBackClick = { onIntent(LoginCodeIntent.OnBackClicked) }
            )
        },
        containerColor = LessTheme.colors.backgroundPrimary
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(LessTheme.colors.backgroundPrimary)
                .imePadding()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LessTheme.spacing.medium)
            ) {
                Text(
                    text = stringResource(Res.string.login_code_title),
                    style = LessTheme.typography.title28Bold,
                    color = LessTheme.colors.textIconsBlack,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

                val emailToShow = state.email.ifEmpty { stringResource(Res.string.login_code_default_email) }
                Text(
                    text = stringResource(Res.string.login_code_subtitle, emailToShow),
                    style = LessTheme.typography.body16Regular,
                    color = LessTheme.colors.textIconsBlack,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(LessTheme.spacing.large))

                OtpCodeInput(
                    value = state.code,
                    onValueChange = { onIntent(LoginCodeIntent.OnCodeChanged(it)) },
                    isError = state.codeError != null,
                    isLoading = state.isLoading,
                    errorMessage = state.codeError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(Res.string.login_code_resend),
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.elementsPrimaryBrand,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !state.isLoading) { onIntent(LoginCodeIntent.OnResendCodeClicked) }
                        .padding(bottom = LessTheme.spacing.xLarge),
                    textAlign = TextAlign.Start
                )
            }

            // Center loading indicator
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center),
                    color = LessTheme.colors.elementsPrimaryBrand,
                    strokeWidth = 4.dp
                )
            }

            // Toast
            AnimatedToast(
                visible = toastState != null,
                title = toastState?.message ?: "",
                type = toastState?.type ?: ToastType.Success,
                modifier = Modifier.align(Alignment.TopCenter),
                showGradientScrim = false
            )
        }
    }
}
