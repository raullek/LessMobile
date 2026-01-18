package az.less.mobile.presentation.client.onboarding.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsToolBar
import az.less.mobile.presentation.client.onboarding.otp.components.OtpCodeInput
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.login_code_title
import lessmobile.composeapp.generated.resources.login_code_subtitle
import lessmobile.composeapp.generated.resources.login_code_default_email
import lessmobile.composeapp.generated.resources.login_code_resend
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginCodeScreen(
    navController: NavController,
    email: String = "", // Can be passed from previous screen
    viewModel: LoginCodeViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginCodeSideEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is LoginCodeSideEffect.NavigateNext -> {
                // Navigate back to More screen after successful login
                // Pop back to the root of the flow (More screen)
                navController.popBackStack("more", inclusive = false)
            }

            is LoginCodeSideEffect.ShowError -> {
                // TODO: Show error message (e.g., snackbar)
            }

            is LoginCodeSideEffect.ShowSuccess -> {
                // TODO: Show success message (e.g., snackbar)
            }
        }
    }

    LoginCodeScreenContent(
        state = state.copy(email = email.ifEmpty { state.email }),
        onIntent = viewModel::onIntent
    )
}

@Composable
fun LoginCodeScreenContent(
    state: LoginCodeState,
    onIntent: (LoginCodeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            DsToolBar(
                title = "", // No title as per design
                onBackClick = { onIntent(LoginCodeIntent.OnBackClicked) }
            )
        },
        containerColor = LessTheme.colors.backgroundPrimary
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(LessTheme.colors.backgroundPrimary)
                .imePadding()
                .padding(innerPadding)
                .padding(horizontal = LessTheme.spacing.medium)
        ) {
            // Title Text Field
            Text(
                text = stringResource(Res.string.login_code_title),
                style = LessTheme.typography.title28Bold,
                color = LessTheme.colors.textIconsBlack,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

            // Subtitle Text Field
            val emailToShow = state.email.ifEmpty { stringResource(Res.string.login_code_default_email) }
            Text(
                text = stringResource(Res.string.login_code_subtitle, emailToShow),
                style = LessTheme.typography.body16Regular,
                color = LessTheme.colors.textIconsBlack,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            // OTP Code Input
            OtpCodeInput(
                value = state.code,
                onValueChange = { onIntent(LoginCodeIntent.OnCodeChanged(it)) },
                isError = state.codeError != null,
                errorMessage = state.codeError,
                modifier = Modifier.fillMaxWidth()
            )

            // Push "Didn't receive it?" to bottom
            Spacer(modifier = Modifier.weight(1f))

            // "Didn't receive it?" Link at bottom
            Text(
                text = stringResource(Res.string.login_code_resend),
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.elementsPrimaryBrand,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(LoginCodeIntent.OnResendCodeClicked) },
                textAlign = TextAlign.Start
            )
        }
    }
}

