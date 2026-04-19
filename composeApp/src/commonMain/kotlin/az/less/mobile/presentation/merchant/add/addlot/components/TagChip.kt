package az.less.mobile.presentation.merchant.add.addlot.components

import androidx.compose.foundation.background
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
import az.less.designsystem.base.LessTheme
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun TagChip(
    label: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
    imageUrl: String? = null
) {
    val backgroundColor = if (isSelected) {
        LessTheme.colors.elementsPrimaryBrand
    } else {
        LessTheme.colors.backgroundSecond
    }

    val contentColor = if (isSelected) {
        LessTheme.colors.textIconsNested
    } else {
        LessTheme.colors.textIconsBlack
    }

    val iconTint = if (isSelected) {
        LessTheme.colors.textIconsNested
    } else {
        Color.Unspecified
    }

    Row(
        modifier = modifier
            .height(LessTheme.size.xxLarge)
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(color = backgroundColor)
            .clickable { onToggle() }
            .padding(LessTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
    ) {
        when {
            imageUrl != null -> {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = label,
                    modifier = Modifier.size(LessTheme.size.medium)
                )
            }
            icon != null -> {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(LessTheme.size.medium)
                )
            }
        }
        Text(
            text = label,
            style = LessTheme.typography.body14Semibold,
            color = contentColor
        )
    }
}
