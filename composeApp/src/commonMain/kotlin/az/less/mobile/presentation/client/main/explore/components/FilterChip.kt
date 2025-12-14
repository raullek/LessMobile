package az.less.mobile.presentation.client.main.explore.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.explore.models.FilterIconType
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_filter_24dp
import lessmobile.composeapp.generated.resources.ic_heart_20dp
import org.jetbrains.compose.resources.painterResource

/**
 * Filter chip component for Explore screen
 * Unified component that handles all filter types: filter button, liked, and dynamic filters
 */
@Composable
fun FilterChip(
    text: String? = null,
    iconType: az.less.mobile.presentation.client.main.explore.models.FilterIconType = _root_ide_package_.az.less.mobile.presentation.client.main.explore.models.FilterIconType.NONE,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = LessTheme.colors.backgroundPrimary
) {
    val defaultBackgroundColor = if (isSelected) {
        LessTheme.colors.elementsPrimaryBrand
    } else {
        backgroundColor
    }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(defaultBackgroundColor)
            .clickable(onClick = onClick)
            .padding(
                horizontal = LessTheme.spacing.medium,
                vertical = LessTheme.spacing.small
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon based on type
            when (iconType) {
                _root_ide_package_.az.less.mobile.presentation.client.main.explore.models.FilterIconType.FILTER -> {
                    Icon(
                        painter = painterResource(Res.drawable.ic_filter_24dp),
                        contentDescription = "Filter",
                        tint = if (isSelected) {
                            LessTheme.colors.surfaceWhite
                        } else {
                            LessTheme.colors.textIconsBlack
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
                _root_ide_package_.az.less.mobile.presentation.client.main.explore.models.FilterIconType.HEART -> {
                    Icon(
                        painter = painterResource(Res.drawable.ic_heart_20dp),
                        contentDescription = "Liked",
                        tint = if (isSelected) {
                            LessTheme.colors.surfaceWhite
                        } else {
                            LessTheme.colors.textIconsBlack
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
                _root_ide_package_.az.less.mobile.presentation.client.main.explore.models.FilterIconType.NONE -> {
                    // No icon
                }
            }

            // Spacer between icon and text (only if both exist)
            if (iconType != _root_ide_package_.az.less.mobile.presentation.client.main.explore.models.FilterIconType.NONE && text != null) {
                Spacer(modifier = Modifier.width(LessTheme.spacing.xSmall))
            }

            // Text
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

