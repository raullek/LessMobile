package az.less.mobile.presentation.client.account.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsSelectionBottomSheet
import az.less.designsystem.components.DsSelectionField
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.DsToolBar
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.test_merchant_logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AccountSideEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is AccountSideEffect.ShowError -> {
            }
        }
    }

    AccountScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun AccountScreenContent(
    state: AccountState,
    onIntent: (AccountIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.showGenderBottomSheet) {
        if (state.showGenderBottomSheet) {
            focusManager.clearFocus()
        }
    }

    Scaffold(
        topBar = {
            DsToolBar(
                title = "Account",
                onBackClick = { onIntent(AccountIntent.OnBackClicked) }
            )
        },
        containerColor = LessTheme.colors.backgroundPrimary
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(innerPadding)
                .padding(
                    horizontal = LessTheme.spacing.medium,
                    vertical = LessTheme.spacing.large
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 2.dp,
                        color = LessTheme.colors.elementsPrimaryBrand,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.test_merchant_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip( RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            DsButton(
                text = "Edit profile photo",
                size = ButtonSize.Medium,
                onClick = { onIntent(AccountIntent.OnEditPhotoClicked) },
                variant = ButtonVariant.Primary
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))

            DsTextField(
                value = state.fullName,
                onValueChange = { onIntent(AccountIntent.OnFullNameChanged(it)) },
                placeholder = "Name Surname",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            DsTextField(
                value = state.phoneNumber,
                onValueChange = { onIntent(AccountIntent.OnPhoneChanged(it)) },
                placeholder = "Phone number",
                isError = state.phoneNumberError != null,
                errorMessage = state.phoneNumberError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            DsTextField(
                value = state.email,
                onValueChange = { onIntent(AccountIntent.OnEmailChanged(it)) },
                placeholder = "Email",
                isError = state.emailError != null,
                errorMessage = state.emailError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            DsSelectionField(
                value = state.gender.takeIf { it.isNotEmpty() },
                placeholder = "Gender",
                onClick = { onIntent(AccountIntent.OnGenderClick) },
                onClear = { onIntent(AccountIntent.OnGenderClear) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            DsSelectionField(
                value = state.birthDate.takeIf { it.isNotEmpty() },
                placeholder = "Birth day",
                onClick = { onIntent(AccountIntent.OnBirthDateClick) },
                onClear = { onIntent(AccountIntent.OnBirthDateClear) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))

            DsButton(
                text = "Delete account",
                textColor = LessTheme.colors.textIconsError,
                onClick = { onIntent(AccountIntent.OnDeleteAccountClicked) },
                variant = ButtonVariant.Secondary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            DsButton(
                text = "Save",
                onClick = {
                    focusManager.clearFocus()
                    onIntent(AccountIntent.OnSaveClicked)
                },
                variant = ButtonVariant.Primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        if (state.showGenderBottomSheet) {
            DsSelectionBottomSheet(
                title = "Select Gender",
                items = state.genderList,
                selectedItem = state.gender.takeIf { it.isNotEmpty() },
                onSelect = { gender ->
                    onIntent(AccountIntent.OnGenderChanged(gender))
                },
                onDismiss = {
                    onIntent(AccountIntent.OnGenderBottomSheetDismiss)
                },
                itemTitle = { it }
            )
        }
    }
}

