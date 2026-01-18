package az.less.mobile.presentation.main.more.paymentmethods.addnewcard

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.DsToolBar
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.add_card_title
import lessmobile.composeapp.generated.resources.add_card_number_placeholder
import lessmobile.composeapp.generated.resources.add_card_expiry_placeholder
import lessmobile.composeapp.generated.resources.add_card_cvv_placeholder
import lessmobile.composeapp.generated.resources.add_card_verification_text
import lessmobile.composeapp.generated.resources.add_card_button
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AddNewCardScreen(
    viewModel: AddNewCardViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AddNewCardSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is AddNewCardSideEffect.NavigateToSuccess -> {
                // TODO: Navigate to success screen
                navController.popBackStack()
            }
            is AddNewCardSideEffect.ShowError -> {
                // TODO: Show error message
            }
        }
    }

    AddNewCardScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun AddNewCardScreenContent(
    state: AddNewCardState,
    onIntent: (AddNewCardIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LessTheme.colors.backgroundPrimary)
    ) {
        DsToolBar(
            title = stringResource(Res.string.add_card_title),
            onBackClick = { onIntent(AddNewCardIntent.OnBackClicked) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium)
                .padding(bottom = LessTheme.spacing.medium)
        ) {
            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            // Card Number Field
            val cardNumberInteractionSource = remember { MutableInteractionSource() }
            DsTextField(
                value = formatCardNumber(state.cardNumber),
                onValueChange = { value ->
                    val digitsOnly = value.filter { it.isDigit() }
                    onIntent(AddNewCardIntent.OnCardNumberChanged(digitsOnly))
                },
                placeholder = stringResource(Res.string.add_card_number_placeholder),
                isError = state.cardNumberError != null,
                errorMessage = state.cardNumberError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                interactionSource = cardNumberInteractionSource
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            // Expiration Date and CVV Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
            ) {
                // Expiration Date
                val expirationDateInteractionSource = remember { MutableInteractionSource() }
                DsTextField(
                    value = state.expirationDate,
                    onValueChange = { value ->
                        onIntent(AddNewCardIntent.OnExpirationDateChanged(value))
                    },
                    placeholder = stringResource(Res.string.add_card_expiry_placeholder),
                    isError = state.expirationDateError != null,
                    errorMessage = state.expirationDateError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    interactionSource = expirationDateInteractionSource
                )

                // CVV
                val cvvInteractionSource = remember { MutableInteractionSource() }
                DsTextField(
                    value = state.cvv,
                    onValueChange = { value ->
                        onIntent(AddNewCardIntent.OnCvvChanged(value))
                    },
                    placeholder = stringResource(Res.string.add_card_cvv_placeholder),
                    isError = state.cvvError != null,
                    errorMessage = state.cvvError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    interactionSource = cvvInteractionSource
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            // Verification Text
            Text(
                text = stringResource(Res.string.add_card_verification_text),
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsGrey,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            // Add Card Button
            DsButton(
                text = stringResource(Res.string.add_card_button),
                onClick = { onIntent(AddNewCardIntent.OnAddCardClicked) },
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                enabled = !state.isLoading
            )
        }
    }
}

private fun formatCardNumber(cardNumber: String): String {
    val digitsOnly = cardNumber.filter { it.isDigit() }
    return digitsOnly.chunked(4).joinToString(" ").take(19) // Max: 0000 0000 0000 0000
}

