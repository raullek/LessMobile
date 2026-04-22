package az.less.mobile.presentation.partner.places.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsTextField
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.edit_profile_edit_phone_number
import lessmobile.composeapp.generated.resources.edit_profile_phone_number
import lessmobile.composeapp.generated.resources.edit_profile_save
import org.jetbrains.compose.resources.stringResource
import io.github.skeptick.inputmask.compose.phone.rememberPhoneInputMaskVisualTransformation
import io.github.skeptick.inputmask.core.InputMasks
import io.github.skeptick.inputmask.core.format

/**
 * Phone number mask for Azerbaijan format: +994 XX XXX XX XX
 * Using special phone mask that handles pasting with or without country code
 */
private const val PHONE_MASK = "+{994} [00] [000] [00] [00]"

/**
 * Input mask instance for formatting phone numbers
 */
private val phoneInputMask = InputMasks.getOrCreate(PHONE_MASK)

/**
 * Bottom sheet for editing phone number
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2356-9855
 */
/**
 * Extract raw digits from a phone number, removing +994 prefix and any formatting
 */
private fun extractRawDigits(phoneNumber: String): String {
    // Remove all non-digit characters
    val digits = phoneNumber.filter { it.isDigit() }
    // Remove 994 prefix if present (country code)
    return if (digits.startsWith("994")) {
        digits.removePrefix("994")
    } else {
        digits
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPhoneNumberBottomSheet(
    isVisible: Boolean,
    sheetState: SheetState,
    initialPhoneNumber: String,
    onSave: (phoneNumber: String) -> Unit,
    onDismiss: () -> Unit
) {
    // Local state for editing - store raw digits only (without +994 prefix)
    var phoneNumber by remember(initialPhoneNumber) { 
        mutableStateOf(extractRawDigits(initialPhoneNumber)) 
    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = LessTheme.colors.backgroundPrimary,
            contentColor = LessTheme.colors.textIconsBlack,
            shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Custom drag handle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(LessTheme.colors.borderPrimary)
                    )
                }

                // Check if phone number is valid (matches the mask completely)
                val formatResult = phoneInputMask.format(phoneNumber)
                val isPhoneValid = formatResult.isComplete

                // Content
                EditPhoneNumberBottomSheetContent(
                    phoneNumber = phoneNumber,
                    isPhoneValid = isPhoneValid,
                    onPhoneNumberChange = { phoneNumber = it },
                    onSaveClick = {
                        if (isPhoneValid) {
                            onSave(formatResult.formattedValue)
                        }
                    }
                )
            }
        }
    }
}

/**
 * Stateless content for EditPhoneNumberBottomSheet
 */
@Composable
private fun EditPhoneNumberBottomSheetContent(
    phoneNumber: String,
    isPhoneValid: Boolean,
    onPhoneNumberChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Phone mask with +994 prefix - handles pasting with or without country code
    val phoneVisualTransformation = rememberPhoneInputMaskVisualTransformation(PHONE_MASK)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium)
            .padding(bottom = 34.dp) // Home indicator space
    ) {
        // Title
        Text(
            text = stringResource(Res.string.edit_profile_edit_phone_number),
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Phone Number TextField with mask
        DsTextField(
            value = phoneNumber,
            onValueChange = { newValue ->
                // Sanitize input using the mask
                val sanitized = phoneVisualTransformation.sanitize(newValue)
                onPhoneNumberChange(sanitized)
            },
            label = stringResource(Res.string.edit_profile_phone_number),
            placeholder = "+994 XX XXX XX XX",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            visualTransformation = phoneVisualTransformation,
            onEndIconClick = { onPhoneNumberChange("") }
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Save button - disabled when phone number is not valid
        DsButton(
            text = stringResource(Res.string.edit_profile_save),
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            variant = ButtonVariant.Primary,
            size = ButtonSize.Large,
            enabled = isPhoneValid
        )
    }
}

