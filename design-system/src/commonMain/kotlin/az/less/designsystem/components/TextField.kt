package az.less.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.design_system.generated.resources.Res
import lessmobile.design_system.generated.resources.ic_clear_rounded_24dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun DsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    endIcon: Painter? = null,
    onEndIconClick: (() -> Unit)? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    val defaultEndIcon = painterResource(Res.drawable.ic_clear_rounded_24dp)
    val actualEndIcon = endIcon ?: defaultEndIcon

    val height = 64.dp
    val horizontalPadding = LessTheme.spacing.medium  // 16dp
    val iconSize = LessTheme.spacing.large  // 24dp

    val borderColor = when {
        !enabled -> LessTheme.colors.textIconsThird
        isError -> LessTheme.colors.textIconsError
        isFocused -> LessTheme.colors.textIconsThird
        else -> androidx.compose.ui.graphics.Color.Transparent
    }

    val backgroundColor = when {
        !enabled -> LessTheme.colors.elementsSecondaryElement.copy(alpha = 0.5f)
        else -> LessTheme.colors.elementsPrimaryElement
    }

    val textColor = when {
        !enabled -> LessTheme.colors.textIconsThird
        else -> LessTheme.colors.textIconsBlack
    }

    val labelColor = when {
        !enabled -> LessTheme.colors.textIconsThird
        isError -> LessTheme.colors.textIconsError
        else -> LessTheme.colors.textIconsGrey
    }

    Column(modifier = modifier) {
        // Label
        if (label != null) {
            Text(
                text = label,
                style = LessTheme.typography.body14Regular,
                color = labelColor,
                modifier = Modifier.padding(bottom = LessTheme.spacing.xSmall)
            )
        }

        // Text Field
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
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
            enabled = enabled,
            textStyle = LessTheme.typography.body16Regular.copy(color = textColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(LessTheme.colors.elementsPrimaryBrand),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Placeholder
                    if (value.isEmpty() && placeholder != null) {
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
                                end = if (value.isNotEmpty()) iconSize + LessTheme.spacing.xSmall else 0.dp
                            )
                    ) {
                        innerTextField()
                    }

                    // End Icon - only visible when text is not empty
                    if (value.isNotEmpty()) {
                        Box(
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            if (onEndIconClick != null) {
                                IconButton(
                                    onClick = onEndIconClick,
                                    enabled = enabled
                                ) {
                                    Icon(
                                        painter = actualEndIcon,
                                        contentDescription = null,
                                        tint = androidx.compose.ui.graphics.Color.Unspecified
                                    )
                                }
                            } else {
                                Icon(
                                    painter = actualEndIcon,
                                    contentDescription = null,
                                    tint = androidx.compose.ui.graphics.Color.Unspecified
                                )
                            }
                        }
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

