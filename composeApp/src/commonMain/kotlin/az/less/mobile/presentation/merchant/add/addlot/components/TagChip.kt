package az.less.mobile.presentation.merchant.add.addlot.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.merchant.add.addlot.model.ChipOption
import org.jetbrains.compose.resources.painterResource

@Composable
fun TagChip(
    option: ChipOption,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        LessTheme.colors.elementsSecondaryBrand
    } else {
        LessTheme.colors.backgroundSecond
    }

    val textColor = if (isSelected) {
        LessTheme.colors.elementsPrimaryBrand
    } else {
        LessTheme.colors.textIconsBlack
    }

    val borderModifier = if (isSelected) {
        Modifier.border(
            width = 2.dp,
            color = LessTheme.colors.elementsPrimaryBrand,
            shape = RoundedCornerShape(LessTheme.radius.medium)
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .height(LessTheme.size.xxLarge)
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .then(borderModifier)
            .background(color = backgroundColor)
            .clickable { onToggle() }
            .padding(LessTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
    ) {
        option.icon?.let { icon ->
            Icon(
                painter = painterResource(icon),
                contentDescription = option.label,
                tint = Color.Unspecified,
                modifier = Modifier.size(LessTheme.size.medium)
            )
        }
        Text(
            text = option.label,
            style = LessTheme.typography.body14Semibold,
            color = textColor
        )
    }
}
