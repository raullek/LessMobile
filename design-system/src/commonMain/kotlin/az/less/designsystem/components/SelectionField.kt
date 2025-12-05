package az.less.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import az.less.designsystem.DsIcons
import az.less.designsystem.base.LessTheme

/**
 * Non-editable selection field for things like Gender, Country, DatePicker, etc.
 *
 * - Shows placeholder when value is null/blank
 * - Whole field is clickable → opens bottom sheet / picker
 */
@Composable
fun DsSelectionField(
    value: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    endIcon: Painter = DsIcons.ArrowDown,
    onEndIconClick: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
) {
    val height = 64.dp
    val horizontalPadding = LessTheme.spacing.medium

    val backgroundColor = LessTheme.colors.elementsSecondaryElement

    val borderColor = when {
        !enabled -> LessTheme.colors.textIconsThird
        isError -> LessTheme.colors.textIconsError
        else -> androidx.compose.ui.graphics.Color.Transparent
    }

    val textColor = if (value.isNullOrBlank()) {
        LessTheme.colors.textIconsThird
    } else {
        LessTheme.colors.textIconsBlack
    }

    val labelColor = when {
        !enabled -> LessTheme.colors.textIconsThird
        isError -> LessTheme.colors.textIconsError
        else -> LessTheme.colors.textIconsGrey
    }

    Column(modifier = modifier) {

        // Label (üst başlıq, optional)
        if (label != null) {
            Text(
                text = label,
                style = LessTheme.typography.body14Regular,
                color = labelColor,
                modifier = Modifier.padding(bottom = LessTheme.spacing.xSmall)
            )
        }

        Box(
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
                )
                .padding(horizontal = horizontalPadding),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = value.takeUnless { it.isNullOrBlank() } ?: placeholder,
                    style = LessTheme.typography.body16Regular,
                    color = textColor,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = enabled) { onClick() }
                )

                if ((onEndIconClick != null || onClear != null) && !value.isNullOrBlank()) {
                    IconButton(
                        onClick = {
                            // If onClear is provided, use it; otherwise use onEndIconClick
                            onClear?.invoke() ?: onEndIconClick?.invoke()
                        },
                        enabled = enabled
                    ) {
                        Icon(
                            painter = endIcon,
                            contentDescription = null,
                            tint = LessTheme.colors.textIconsThird
                        )
                    }
                } else {
                    Icon(
                        painter = endIcon,
                        contentDescription = null,
                        tint = LessTheme.colors.textIconsThird,
                        modifier = Modifier.clickable(enabled = enabled) { onClick() }
                    )
                }
            }
        }

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
