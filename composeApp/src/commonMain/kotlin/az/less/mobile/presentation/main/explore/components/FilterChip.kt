package az.less.mobile.presentation.main.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_heart_20dp
import org.jetbrains.compose.resources.painterResource

/**
 * Filter chip component for Explore screen
 * Selectable chip with optional icon container and text
 */
@Composable
fun FilterChip(
    text: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showIconContainer: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(
                if (isSelected) {
                    LessTheme.colors.elementsPrimaryBrand
                } else {
                    LessTheme.colors.backgroundPrimary
                }
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = LessTheme.spacing.medium,
                vertical = LessTheme.spacing.small
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            if (showIconContainer) {
                Icon(
                    painter = painterResource(Res.drawable.ic_heart_20dp),
                    contentDescription = "liked",
                    tint = if (isSelected) {
                        LessTheme.colors.surfaceWhite
                    } else {
                        LessTheme.colors.textIconsBlack
                    },
                    modifier = Modifier.size(20.dp) // Icon size 20dp
                )

                Spacer(modifier = Modifier.width(LessTheme.spacing.xSmall))
            }

            if (text != null) {
                Text(
                    text = text,
                    style = LessTheme.typography.body14Semibold,
                    color = if (isSelected) {
                        LessTheme.colors.surfaceWhite
                    } else {
                        LessTheme.colors.textIconsBlack
                    }
                )
            }
        }
    }
}

