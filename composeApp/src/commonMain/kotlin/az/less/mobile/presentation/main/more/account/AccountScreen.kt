package az.less.mobile.presentation.main.more.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
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
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(LessTheme.colors.elementsPrimaryElement)
                    .border(
                        width = 2.dp,
                        color = LessTheme.colors.elementsPrimaryBrand,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.test_merchant_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(112.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            DsButton(
                text = "Edit profile photo",
                textColor = LessTheme.colors.elementsPrimaryBrand,
                onClick = { onIntent(AccountIntent.OnEditPhotoClicked) },
                variant = ButtonVariant.Secondary
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))

            DsTextField(
                value = state.fullName,
                onValueChange = { onIntent(AccountIntent.OnFullNameChanged(it)) },
                placeholder = "Name Surname",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            PhoneNumberTextField(
                value = state.phoneNumber,
                onValueChange = { onIntent(AccountIntent.OnPhoneChanged(it)) },
                placeholder = "Phone number",
                isError = state.phoneNumberError != null,
                errorMessage = state.phoneNumberError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            DsTextField(
                value = state.email,
                onValueChange = { onIntent(AccountIntent.OnEmailChanged(it)) },
                placeholder = "Email",
                isError = state.emailError != null,
                errorMessage = state.emailError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            DsSelectionField(
                value = state.gender.takeIf { it.isNotEmpty() },
                placeholder = "Gender",
                onClick = { onIntent(AccountIntent.OnGenderClick) },
                onClear = { onIntent(AccountIntent.OnGenderClear) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            DsSelectionField(
                value = state.birthDate.takeIf { it.isNotEmpty() },
                placeholder = "Birth day",
                onClick = { onIntent(AccountIntent.OnBirthDateClick) },
                onClear = { onIntent(AccountIntent.OnBirthDateClear) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            DsButton(
                text = "Delete account",
                textColor = LessTheme.colors.textIconsError,
                onClick = { onIntent(AccountIntent.OnDeleteAccountClicked) },
                variant = ButtonVariant.Secondary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

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

@Composable
private fun PhoneNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    // Use TextFieldValue to control cursor position
    var textFieldValue by remember(value, isFocused) {
        mutableStateOf(
            if (isFocused) {
                TextFieldValue("+994$value", TextRange(4 + value.length))
            } else {
                TextFieldValue(value)
            }
        )
    }
    
    // Update when focus changes
    LaunchedEffect(isFocused) {
        if (isFocused) {
            // Focus gained: add +994 prefix and set cursor after prefix
            val currentValue = if (textFieldValue.text.startsWith("+994")) {
                textFieldValue.text.substring(4)
            } else {
                textFieldValue.text
            }
            textFieldValue = TextFieldValue("+994$currentValue", TextRange(4 + currentValue.length))
        } else {
            // Focus lost: keep +994 prefix in display
            val currentValue = if (textFieldValue.text.startsWith("+994")) {
                textFieldValue.text.substring(4)
            } else {
                textFieldValue.text
            }
            // Show +994 prefix even when not focused (if there's a value)
            textFieldValue = if (currentValue.isNotEmpty()) {
                TextFieldValue("+994$currentValue")
            } else {
                TextFieldValue("")
            }
        }
    }
    
    // Update when external value changes (but not when focused to avoid cursor jumping)
    LaunchedEffect(value) {
        if (!isFocused) {
            // Show +994 prefix even when not focused (if there's a value)
            textFieldValue = if (value.isNotEmpty()) {
                TextFieldValue("+994$value")
            } else {
                TextFieldValue("")
            }
        }
    }
    
    val borderColor = when {
        isError -> LessTheme.colors.textIconsError
        isFocused -> LessTheme.colors.textIconsThird
        else -> androidx.compose.ui.graphics.Color.Transparent
    }
    
    val backgroundColor = LessTheme.colors.elementsPrimaryElement
    val textColor = LessTheme.colors.textIconsBlack
    val height = 64.dp
    val horizontalPadding = LessTheme.spacing.medium
    val iconSize = LessTheme.spacing.large
    
    Column(modifier = modifier) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                // Prevent deleting +994 prefix when focused
                if (isFocused) {
                    if (newValue.text.length < 4 || !newValue.text.startsWith("+994")) {
                        // User tried to delete +994, restore it
                        val currentValue = if (textFieldValue.text.startsWith("+994")) {
                            textFieldValue.text.substring(4)
                        } else {
                            ""
                        }
                        textFieldValue = TextFieldValue("+994$currentValue", TextRange(4 + currentValue.length))
                        onValueChange(currentValue)
                        return@BasicTextField
                    }
                    
                    // Extract value after +994
                    val cleanedValue = newValue.text.substring(4)
                    val cursorOffset = newValue.selection.start - 4
                    val newCursorPosition = cursorOffset.coerceIn(0, cleanedValue.length)
                    
                    textFieldValue = TextFieldValue("+994$cleanedValue", TextRange(4 + newCursorPosition))
                    onValueChange(cleanedValue)
                } else {
                    // Not focused: allow normal editing
                    textFieldValue = newValue
                    onValueChange(newValue.text)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(LessTheme.radius.small)
                )
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(LessTheme.radius.small)
                ),
            textStyle = LessTheme.typography.body16Regular.copy(color = textColor),
            singleLine = true,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(LessTheme.colors.elementsPrimaryBrand),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Placeholder - only show when value is empty and not focused
                    if (textFieldValue.text.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = LessTheme.typography.body16Regular,
                            color = LessTheme.colors.textIconsThird
                        )
                    }
                    
                    // Input text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                end = if (textFieldValue.text.isNotEmpty()) iconSize + LessTheme.spacing.xSmall else 0.dp
                            )
                    ) {
                        innerTextField()
                    }
                }
            }
        )
        
        // Error Message
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsError,
                modifier = Modifier.padding(top = LessTheme.spacing.xxSmall)
            )
        }
    }
}