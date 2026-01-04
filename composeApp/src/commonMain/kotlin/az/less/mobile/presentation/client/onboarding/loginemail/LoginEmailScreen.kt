package az.less.mobile.presentation.client.onboarding.loginemail

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.DsToolBar
import az.less.mobile.navigation.MoreScreens
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginEmailScreen(
    navController: NavController,
    viewModel: LoginEmailViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginEmailSideEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is LoginEmailSideEffect.NavigateNext -> {
                navController.navigate(MoreScreens.LoginCode.createRoute(state.email))
            }

            is LoginEmailSideEffect.ShowError -> {
                // TODO: Show error message (e.g., snackbar)
            }
        }
    }

    LoginEmailScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun LoginEmailScreenContent(
    state: LoginEmailState,
    onIntent: (LoginEmailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            DsToolBar(
                title = "Email",
                onBackClick = { onIntent(LoginEmailIntent.OnBackClicked) }
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
            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            // Email Input Field
            DsTextField(
                value = state.email,
                onValueChange = { onIntent(LoginEmailIntent.OnEmailChanged(it)) },
                placeholder = "Mail",
                isError = state.emailError != null,
                errorMessage = state.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                onEndIconClick = {onIntent(LoginEmailIntent.OnEmailChanged(""))},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Next Button
            DsButton(
                text = "Next",
                onClick = { onIntent(LoginEmailIntent.OnNextClicked) },
                variant = ButtonVariant.Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LessTheme.spacing.medium)
            )
        }
    }
}

