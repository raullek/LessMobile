package az.less.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme

enum class ButtonVariant {
    Primary,
    Secondary,
    Tertiary
}

enum class ButtonSize {
    Large,  // 56dp height
    Medium  // 48dp height
}

@Composable
fun DsButton(
    text: String,
    textColor: Color? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Large,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val colors = when (variant) {
        ButtonVariant.Primary -> ButtonDefaults.buttonColors(
            containerColor = when {
                !enabled -> LessTheme.colors.elementsThirdElement
                isPressed -> LessTheme.colors.elementPressedPrimaryBrand
                else -> LessTheme.colors.elementsPrimaryBrand
            },
            contentColor = when {
                !enabled -> LessTheme.colors.textIconsThird
                else -> LessTheme.colors.textIconsNested
            },
            disabledContainerColor = LessTheme.colors.elementsThirdElement,
            disabledContentColor = LessTheme.colors.textIconsThird
        )

        ButtonVariant.Secondary -> ButtonDefaults.buttonColors(
            containerColor = when {
                !enabled -> LessTheme.colors.elementsSecondaryElement.copy(alpha = 0.5f)
                isPressed -> LessTheme.colors.elementPressedSecondaryElement
                else -> LessTheme.colors.elementsSecondaryElement
            },
            contentColor = when {
                !enabled -> LessTheme.colors.textIconsThird
                else -> LessTheme.colors.textIconsBlack
            },
            disabledContainerColor = LessTheme.colors.elementsSecondaryElement.copy(alpha = 0.5f),
            disabledContentColor = LessTheme.colors.textIconsThird
        )

        ButtonVariant.Tertiary -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (!enabled) {
                LessTheme.colors.textIconsThird
            } else {
                LessTheme.colors.textIconsBrand
            },
            disabledContainerColor = Color.Transparent,
            disabledContentColor = LessTheme.colors.textIconsThird
        )
    }

    val height = when (size) {
        ButtonSize.Large -> LessTheme.spacing.huge  // 56dp
        ButtonSize.Medium -> LessTheme.spacing.xxxLarge  // 48dp
    }

    val horizontalPadding = when (size) {
        ButtonSize.Large -> LessTheme.spacing.xLarge  // 32dp
        ButtonSize.Medium -> LessTheme.spacing.large  // 24dp
    }

    val iconSize = when (size) {
        ButtonSize.Large -> LessTheme.spacing.large  // 24dp
        ButtonSize.Medium -> LessTheme.spacing.medium  // 16dp
    }

    val loaderSize = when (size) {
        ButtonSize.Large -> 24.dp
        ButtonSize.Medium -> 20.dp
    }

    Button(
        onClick = { if (!isLoading) onClick() },
        modifier = modifier.defaultMinSize(minHeight = height),
        enabled = enabled,
        shape = RoundedCornerShape(LessTheme.radius.small),  // 12dp
        colors = colors,
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 0.dp),
        interactionSource = interactionSource,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(loaderSize),
                color = LessTheme.colors.textIconsNested,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                    Spacer(modifier = Modifier.width(LessTheme.spacing.xSmall))  // 8dp
                }
                Text(
                    text = text,
                    style = LessTheme.typography.body16Semibold,
                    color = textColor ?: LocalContentColor.current
                )
            }
        }
    }
}
