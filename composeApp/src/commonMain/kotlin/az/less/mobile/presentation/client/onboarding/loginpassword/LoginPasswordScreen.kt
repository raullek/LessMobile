package az.less.mobile.presentation.client.onboarding.loginpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.AnimatedToast
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.DsToolBar
import az.less.designsystem.components.ToastType
import az.less.mobile.navigation.ClientRoute
import az.less.mobile.presentation.client.onboarding.otp.ToastState
import kotlinx.coroutines.delay
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.login_password_title
import lessmobile.composeapp.generated.resources.login_email_placeholder
import lessmobile.composeapp.generated.resources.login_password_placeholder
import lessmobile.composeapp.generated.resources.login_password_button
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import androidx.compose.runtime.LaunchedEffect

@Composable
fun LoginPasswordScreen(
    navController: NavController,
    navigateToMerchant: () -> Unit = {},
    viewModel: LoginPasswordViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    var toastState by remember { mutableStateOf<ToastState?>(null) }

    LaunchedEffect(toastState) {
        if (toastState != null) {
            delay(3000)
            toastState = null
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginPasswordSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is LoginPasswordSideEffect.NavigateToClient -> {
                navController.popBackStack<ClientRoute.More>(inclusive = false)
            }
            is LoginPasswordSideEffect.NavigateToMerchant -> {
                navigateToMerchant()
            }
            is LoginPasswordSideEffect.ShowError -> {
                toastState = ToastState(sideEffect.message, ToastType.Error)
            }
        }
    }

    LoginPasswordScreenContent(
        state = state,
        toastState = toastState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun LoginPasswordScreenContent(
    state: LoginPasswordState,
    toastState: ToastState?,
    onIntent: (LoginPasswordIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            DsToolBar(
                title = stringResource(Res.string.login_password_title),
                onBackClick = { onIntent(LoginPasswordIntent.OnBackClicked) }
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
                Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

                DsTextField(
                    value = state.email,
                    onValueChange = { onIntent(LoginPasswordIntent.OnEmailChanged(it)) },
                    placeholder = stringResource(Res.string.login_email_placeholder),
                    isError = state.emailError != null,
                    errorMessage = state.emailError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    onEndIconClick = { onIntent(LoginPasswordIntent.OnEmailChanged("")) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

                DsTextField(
                    value = state.password,
                    onValueChange = { onIntent(LoginPasswordIntent.OnPasswordChanged(it)) },
                    placeholder = stringResource(Res.string.login_password_placeholder),
                    isError = state.passwordError != null,
                    errorMessage = state.passwordError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                DsButton(
                    text = stringResource(Res.string.login_password_button),
                    onClick = { onIntent(LoginPasswordIntent.OnLoginClicked) },
                    variant = ButtonVariant.Primary,
                    enabled = state.isFormValid,
                    isLoading = state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = LessTheme.spacing.medium)
                )
            }

            AnimatedToast(
                visible = toastState != null,
                title = toastState?.message ?: "",
                type = toastState?.type ?: ToastType.Error,
                modifier = Modifier.align(Alignment.TopCenter),
                showGradientScrim = false
            )
        }
    }
}
