package az.less.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import az.less.designsystem.base.LessTheme

@Composable
fun DsCheckBox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = LessTheme.colors.elementsPrimaryBrand,
            uncheckedColor = LessTheme.colors.backgroundPrimary,
            checkmarkColor = LessTheme.colors.textIconsNested,
            disabledCheckedColor = LessTheme.colors.elementsThirdElement,
            disabledUncheckedColor = LessTheme.colors.backgroundPrimary,
            disabledIndeterminateColor = LessTheme.colors.elementsThirdElement
        ),
        interactionSource = interactionSource
    )
}





