package az.less.mobile.presentation.client.account.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.mobile.navigation.ClientRoute
import coil3.compose.AsyncImage
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.skeptick.inputmask.compose.phone.rememberPhoneInputMaskVisualTransformation
import az.less.mobile.utils.displayDateToMillis
import az.less.mobile.utils.millisToDisplayDate
import kotlinx.coroutines.launch
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.AnimatedToast
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsConfirmationBottomSheet
import az.less.designsystem.components.DsSelectionBottomSheet
import az.less.designsystem.components.DsSelectionField
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.TextFieldColors
import az.less.designsystem.components.DsToolBar
import kotlinx.coroutines.delay
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.account_title
import lessmobile.composeapp.generated.resources.account_name_placeholder
import lessmobile.composeapp.generated.resources.account_phone_placeholder
import lessmobile.composeapp.generated.resources.account_email_placeholder
import lessmobile.composeapp.generated.resources.account_gender_placeholder
import lessmobile.composeapp.generated.resources.account_birthday_placeholder
import lessmobile.composeapp.generated.resources.account_delete
import lessmobile.composeapp.generated.resources.account_delete_title
import lessmobile.composeapp.generated.resources.account_delete_description
import lessmobile.composeapp.generated.resources.action_cancel
import lessmobile.composeapp.generated.resources.action_save
import lessmobile.composeapp.generated.resources.account_select_gender
import lessmobile.composeapp.generated.resources.account_profile_photo
import lessmobile.composeapp.generated.resources.ic_edit_24dp
import lessmobile.composeapp.generated.resources.ic_person_image_placeholder_48dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Phone number mask for Azerbaijan format: +994 XX XXX XX XX
 */
private const val PHONE_MASK = "+{994} [00] [000] [00] [00]"


@Composable
fun AccountScreen(
    viewModel: AccountViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    LaunchedEffect(state.toastMessage) {
        if (state.toastMessage != null) {
            delay(3000)
            viewModel.onIntent(AccountIntent.OnToastDismiss)
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AccountSideEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is AccountSideEffect.NavigateToLogin -> {
                navController.navigate(ClientRoute.Welcome) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    AccountScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreenContent(
    state: AccountState,
    onIntent: (AccountIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    // Phone mask with +994 prefix - handles pasting with or without country code
    val phoneVisualTransformation = rememberPhoneInputMaskVisualTransformation(PHONE_MASK)

    LaunchedEffect(state.showGenderBottomSheet) {
        if (state.showGenderBottomSheet) {
            focusManager.clearFocus()
        }
    }

    // Date Picker Dialog
    if (state.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.birthDate.displayDateToMillis()
        )
        DatePickerDialog(
            onDismissRequest = { onIntent(AccountIntent.OnDatePickerDismiss) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            onIntent(AccountIntent.OnDatePickerConfirm(millis.millisToDisplayDate()))
                        } else {
                            onIntent(AccountIntent.OnDatePickerDismiss)
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = { onIntent(AccountIntent.OnDatePickerDismiss) }
                ) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Profile Photo Picker
    if (state.showProfilePhotoPicker) {
        GalleryPickerLauncher(
            onPhotosSelected = { photos ->
                photos.firstOrNull()?.let { photo ->
                    coroutineScope.launch {
                        val bytes = photo.loadBytes()
                        if (bytes != null) {
                            onIntent(AccountIntent.OnProfilePhotoSelected(bytes))
                        } else {
                            onIntent(AccountIntent.OnProfilePhotoPickerDismiss)
                        }
                    }
                } ?: onIntent(AccountIntent.OnProfilePhotoPickerDismiss)
            },
            onError = { onIntent(AccountIntent.OnProfilePhotoPickerDismiss) },
            onDismiss = { onIntent(AccountIntent.OnProfilePhotoPickerDismiss) },
            allowMultiple = false
        )
    }

    Scaffold(
        topBar = {
            DsToolBar(
                title = stringResource(Res.string.account_title),
                onBackClick = { onIntent(AccountIntent.OnBackClicked) }
            )
        },
        containerColor = LessTheme.colors.backgroundPrimary
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(
                            start = LessTheme.spacing.medium,
                            end = LessTheme.spacing.medium
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                width = 2.dp,
                                color = LessTheme.colors.elementsPrimaryBrand,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .background(LessTheme.colors.elementsSecondaryElement),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.profilePhotoBytes != null) {
                            AsyncImage(
                                model = state.profilePhotoBytes,
                                contentDescription = stringResource(Res.string.account_profile_photo),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(RoundedCornerShape(20.dp))
                            )
                        } else {
                            Image(
                                painter = painterResource(Res.drawable.ic_person_image_placeholder_48dp),
                                contentDescription = stringResource(Res.string.account_profile_photo),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(RoundedCornerShape(20.dp))
                            )
                        }

                        // Edit icon overlay
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0x80171A1C))
                                .clickable { onIntent(AccountIntent.OnEditPhotoClicked) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_edit_24dp),
                                contentDescription = "Edit photo",
                                tint = LessTheme.colors.textIconsNested,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))

                    DsTextField(
                        value = state.fullName,
                        onValueChange = { onIntent(AccountIntent.OnFullNameChanged(it)) },
                        onEndIconClick = { onIntent(AccountIntent.OnFullNameChanged("")) },
                        placeholder = stringResource(Res.string.account_name_placeholder),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(LessTheme.spacing.small))

                    DsTextField(
                        value = state.phoneNumber,
                        onValueChange = { newValue ->
                            val sanitized = phoneVisualTransformation.sanitize(newValue)
                            onIntent(AccountIntent.OnPhoneChanged(sanitized))
                        },
                        onEndIconClick = { onIntent(AccountIntent.OnPhoneChanged("")) },
                        placeholder = "+994 XX XXX XX XX",
                        isError = state.phoneNumberError != null,
                        errorMessage = state.phoneNumberError,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        visualTransformation = phoneVisualTransformation
                    )

                    Spacer(modifier = Modifier.height(LessTheme.spacing.small))

                    DsTextField(
                        value = state.email,
                        onValueChange = {},
                        placeholder = stringResource(Res.string.account_email_placeholder),
                        enabled = false,
                        colors = TextFieldColors(borderColorDisabled = Color.Transparent),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(LessTheme.spacing.small))

                    DsSelectionField(
                        value = state.gender?.displayName,
                        placeholder = stringResource(Res.string.account_gender_placeholder),
                        onClick = { onIntent(AccountIntent.OnGenderClick) },
                        onClear = { onIntent(AccountIntent.OnGenderClear) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(LessTheme.spacing.small))

                    DsSelectionField(
                        value = state.birthDate.ifEmpty { null },
                        placeholder = "DD.MM.YYYY",
                        onClick = { onIntent(AccountIntent.OnBirthDateClick) },
                        onClear = { onIntent(AccountIntent.OnBirthDateClear) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))

                    DsButton(
                        text = stringResource(Res.string.action_save),
                        onClick = {
                            focusManager.clearFocus()
                            onIntent(AccountIntent.OnSaveClicked)
                        },
                        isLoading = state.isLoading,
                        variant = ButtonVariant.Primary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(LessTheme.spacing.small))

                    DsButton(
                        text = stringResource(Res.string.account_delete),
                        textColor = LessTheme.colors.textIconsError,
                        onClick = { onIntent(AccountIntent.OnDeleteAccountClicked) },
                        variant = ButtonVariant.Secondary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
                }
            }

            // Toast
            AnimatedToast(
                visible = state.toastMessage != null,
                title = state.toastMessage ?: "",
                type = state.toastType,
                modifier = Modifier.align(Alignment.TopCenter),
                showGradientScrim = false
            )
        }

        if (state.showGenderBottomSheet) {
            DsSelectionBottomSheet(
                title = stringResource(Res.string.account_select_gender),
                items = Gender.entries,
                selectedItem = state.gender,
                onSelect = { gender ->
                    onIntent(AccountIntent.OnGenderChanged(gender))
                },
                onDismiss = {
                    onIntent(AccountIntent.OnGenderBottomSheetDismiss)
                },
                itemTitle = { it.displayName }
            )
        }

        if (state.showDeleteConfirmation) {
            DsConfirmationBottomSheet(
                image = painterResource(Res.drawable.ic_person_image_placeholder_48dp),
                title = stringResource(Res.string.account_delete_title),
                description = stringResource(Res.string.account_delete_description),
                primaryButtonText = stringResource(Res.string.account_delete),
                secondaryButtonText = stringResource(Res.string.action_cancel),
                onPrimaryClick = { onIntent(AccountIntent.OnDeleteAccountConfirmed) },
                onSecondaryClick = { onIntent(AccountIntent.OnDeleteAccountDismissed) },
                onDismiss = { onIntent(AccountIntent.OnDeleteAccountDismissed) }
            )
        }
    }
}

