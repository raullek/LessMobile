package az.less.mobile.presentation.client.account.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import coil3.compose.AsyncImage
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.skeptick.inputmask.compose.phone.rememberPhoneInputMaskVisualTransformation
import io.github.skeptick.inputmask.compose.rememberInputMaskVisualTransformation
import kotlinx.coroutines.launch
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsSelectionBottomSheet
import az.less.designsystem.components.DsSelectionField
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.DsToolBar
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.account_title
import lessmobile.composeapp.generated.resources.account_name_placeholder
import lessmobile.composeapp.generated.resources.account_phone_placeholder
import lessmobile.composeapp.generated.resources.account_email_placeholder
import lessmobile.composeapp.generated.resources.account_gender_placeholder
import lessmobile.composeapp.generated.resources.account_birthday_placeholder
import lessmobile.composeapp.generated.resources.account_delete
import lessmobile.composeapp.generated.resources.action_save
import lessmobile.composeapp.generated.resources.account_select_gender
import lessmobile.composeapp.generated.resources.account_profile_photo
import lessmobile.composeapp.generated.resources.ic_edit_24dp
import lessmobile.composeapp.generated.resources.image_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Phone number mask for Azerbaijan format: +994 XX XXX XX XX
 */
private const val PHONE_MASK = "+{994} [00] [000] [00] [00]"

/**
 * Date mask for birthday format: DD.MM.YYYY
 */
private const val DATE_MASK = "[00].[00].[0000]"

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
    val coroutineScope = rememberCoroutineScope()

    // Phone mask with +994 prefix - handles pasting with or without country code
    val phoneVisualTransformation = rememberPhoneInputMaskVisualTransformation(PHONE_MASK)

    // Date mask for birthday field
    val dateVisualTransformation = rememberInputMaskVisualTransformation(DATE_MASK)

    LaunchedEffect(state.showGenderBottomSheet) {
        if (state.showGenderBottomSheet) {
            focusManager.clearFocus()
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
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(innerPadding)
                .padding(
                    start = LessTheme.spacing.medium,
                    end = LessTheme.spacing.medium,
                    top = LessTheme.spacing.small,
                    bottom = LessTheme.spacing.large
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (state.profilePhotoBytes != null) {
                    AsyncImage(
                        model = state.profilePhotoBytes,
                        contentDescription = stringResource(Res.string.account_profile_photo),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(Res.drawable.image_placeholder),
                        contentDescription = stringResource(Res.string.account_profile_photo),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(16.dp))
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
                onValueChange = { onIntent(AccountIntent.OnEmailChanged(it)) },
                placeholder = stringResource(Res.string.account_email_placeholder),
                isError = state.emailError != null,
                errorMessage = state.emailError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            DsSelectionField(
                value = state.gender.takeIf { it.isNotEmpty() },
                placeholder = stringResource(Res.string.account_gender_placeholder),
                onClick = { onIntent(AccountIntent.OnGenderClick) },
                onClear = { onIntent(AccountIntent.OnGenderClear) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            DsTextField(
                value = state.birthDate,
                onValueChange = { newValue ->
                    val sanitized = dateVisualTransformation.sanitize(newValue)
                    onIntent(AccountIntent.OnBirthDateChanged(sanitized))
                },
                placeholder = "DD.MM.YYYY",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = dateVisualTransformation
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))

            DsButton(
                text = stringResource(Res.string.account_delete),
                textColor = LessTheme.colors.textIconsError,
                onClick = { onIntent(AccountIntent.OnDeleteAccountClicked) },
                variant = ButtonVariant.Secondary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            DsButton(
                text = stringResource(Res.string.action_save),
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
                title = stringResource(Res.string.account_select_gender),
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

