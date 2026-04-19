package az.less.mobile.presentation.merchant.add.addlot.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme

@Composable
fun TimeSlotButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasError: Boolean = false
) {
    val backgroundColor = if (isSelected) {
        LessTheme.colors.elementsPrimaryBrand
    } else {
        LessTheme.colors.backgroundSecond
    }

    val textColor = if (isSelected) {
        LessTheme.colors.textIconsNested
    } else {
        LessTheme.colors.textIconsBlack
    }

    val errorModifier = if (hasError && !isSelected) {
        Modifier.border(1.dp, LessTheme.colors.textIconsError, RoundedCornerShape(LessTheme.radius.medium))
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .height(LessTheme.size.xxLarge)
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .then(errorModifier)
            .background(color = backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = LessTheme.spacing.small, vertical = LessTheme.spacing.small),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = LessTheme.typography.body14Semibold,
            color = textColor
        )
    }
}
