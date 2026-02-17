package az.less.mobile.presentation.client.onboarding.otp.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import az.less.designsystem.base.LessTheme
import io.github.skeptick.inputmask.compose.rememberInputMaskVisualTransformation
import io.github.skeptick.inputmask.core.InputMasks
import io.github.skeptick.inputmask.core.format

/**
 * Custom OTP Code Input Component
 * Displays 6-digit code in "000-000" format with large, centered text
 */
@Composable
fun OtpCodeInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val textColor = LessTheme.colors.textIconsBrand
    val placeholderColor = if (isError) {
        LessTheme.colors.textIconsError.copy(alpha = 0.5f)
    } else {
        LessTheme.colors.textIconsThird.copy(alpha = 0.5f)
    }

    val visualTransformation = rememberInputMaskVisualTransformation(OTP_CODE_MASK)
    val inputMask = remember { InputMasks.getOrCreate(OTP_CODE_MASK) }
    
    // Auto focus on field
    val focusRequester = remember { FocusRequester() }
    
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Calculate partial placeholder: show entered digits + remaining mask
    val partialPlaceholder = remember(value) {
        if (value.isEmpty()) {
            OTP_CODE_PLACEHOLDER
        } else {
            // Format current value to see what's already entered
            val formatResult = inputMask.format(value)
            val formattedValue = formatResult.formattedValue
            
            // Calculate how many characters are filled
            val filledLength = formattedValue.length
            
            // Build placeholder: formatted value + remaining mask characters
            if (filledLength < OTP_CODE_PLACEHOLDER.length) {
                // Show entered part + remaining placeholder
                formattedValue + OTP_CODE_PLACEHOLDER.substring(filledLength)
            } else {
                formattedValue
            }
        }
    }

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                // Sanitize input using the mask library
                onValueChange(visualTransformation.sanitize(newValue))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .focusRequester(focusRequester),
            enabled = enabled && !isLoading,
            textStyle = TextStyle(
                fontSize = 48.sp, // 48sp as per design
                color = textColor,
                textAlign = TextAlign.Start,
                fontFamily = LessTheme.typography.body16Medium.fontFamily,
                fontWeight = LessTheme.typography.body16Medium.fontWeight
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(LessTheme.colors.elementsPrimaryBrand),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Show partial placeholder when value is not complete
                    if (value.length < OTP_CODE_LENGTH) {
                        Text(
                            text = partialPlaceholder,
                            style = TextStyle(
                                fontSize = 48.sp,
                                color = placeholderColor,
                                textAlign = TextAlign.Start,
                                fontFamily = LessTheme.typography.body16Medium.fontFamily,
                                fontWeight = LessTheme.typography.body16Medium.fontWeight
                            )
                        )
                    }

                    // Input text (will overlay placeholder)
                    innerTextField()
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


private const val OTP_CODE_MASK = "[000]-[000]"

/**
 * Display format for OTP code placeholder
 */
private const val OTP_CODE_PLACEHOLDER = "000-000"

/**
 * Maximum length of OTP code (6 digits)
 */
private const val OTP_CODE_LENGTH = 6

