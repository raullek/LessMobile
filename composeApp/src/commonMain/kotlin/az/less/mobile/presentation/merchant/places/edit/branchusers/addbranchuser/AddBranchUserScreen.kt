package az.less.mobile.presentation.merchant.places.edit.branchusers.addbranchuser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.DsToolBar
import io.github.skeptick.inputmask.compose.phone.rememberPhoneInputMaskVisualTransformation
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Phone number mask for Azerbaijan format: +994 XX XXX XX XX
 * Using special phone mask that handles pasting with or without country code
 */
private const val PHONE_MASK = "+{994} [00] [000] [00] [00]"

/**
 * Stateful AddBranchUserScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun AddBranchUserScreen(
    viewModel: AddBranchUserViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AddBranchUserSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is AddBranchUserSideEffect.UserSaved -> {
                navController.popBackStack()
            }
            is AddBranchUserSideEffect.UserDeleted -> {
                navController.popBackStack()
            }
            is AddBranchUserSideEffect.ShowError -> {
                // Show error snackbar or dialog
            }
        }
    }

    // Render the stateless UI
    AddBranchUserScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless AddBranchUserScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun AddBranchUserScreenContent(
    state: AddBranchUserState,
    onIntent: (AddBranchUserIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Phone mask with +994 prefix - handles pasting with or without country code
    val phoneVisualTransformation = rememberPhoneInputMaskVisualTransformation(PHONE_MASK)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundPrimary)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Toolbar with user number
        DsToolBar(
            title = "User ${state.userNumber}",
            onBackClick = { onIntent(AddBranchUserIntent.OnBackClick) },
            backgroundColor = LessTheme.colors.backgroundPrimary
        )

        // Form fields
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = LessTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
        ) {
            // Name field with clear icon
            DsTextField(
                value = state.name,
                onValueChange = { onIntent(AddBranchUserIntent.OnNameChange(it)) },
                placeholder = "Name",
                isError = state.nameError != null,
                errorMessage = state.nameError,
                onEndIconClick = { onIntent(AddBranchUserIntent.OnNameChange("")) },
                modifier = Modifier.fillMaxWidth()
            )

            // Phone number field with +994 mask and clear icon
            DsTextField(
                value = state.phoneNumber,
                onValueChange = { newValue ->
                    // Sanitize input using the mask
                    val sanitized = phoneVisualTransformation.sanitize(newValue)
                    onIntent(AddBranchUserIntent.OnPhoneNumberChange(sanitized))
                },
                isError = state.phoneError != null,
                errorMessage = state.phoneError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                visualTransformation = phoneVisualTransformation,
                onEndIconClick = { onIntent(AddBranchUserIntent.OnPhoneNumberChange("")) },
                modifier = Modifier.fillMaxWidth()
            )

            // Email field with clear icon
            DsTextField(
                value = state.email,
                onValueChange = { onIntent(AddBranchUserIntent.OnEmailChange(it)) },
                placeholder = "Mail",
                isError = state.emailError != null,
                errorMessage = state.emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onEndIconClick = { onIntent(AddBranchUserIntent.OnEmailChange("")) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Bottom buttons section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LessTheme.spacing.medium,
                    vertical = LessTheme.spacing.medium
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
        ) {

            if (state.isEditMode) {
                DsButton(
                    text = "Delete user",
                    onClick = { onIntent(AddBranchUserIntent.OnDeleteClick) },
                    textColor = LessTheme.colors.textIconsError,
                    variant = ButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth()
                    )
            }
            // Save button
            DsButton(
                text = "Save changes",
                onClick = { onIntent(AddBranchUserIntent.OnSaveClick) },
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large,
                enabled = !state.isLoading
            )
            
            // Delete button - only shown in edit mode

        }
    }
}

