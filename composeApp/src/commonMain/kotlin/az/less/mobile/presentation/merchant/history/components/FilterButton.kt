package az.less.mobile.presentation.merchant.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_down24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Filter button component for Month and Branch filters
 * Based on Figma design with white background, rounded corners, and chevron icon
 */
@Composable
fun FilterButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .height(LessTheme.size.xxLarge) // 48dp
            .background(
                color = LessTheme.colors.backgroundPrimary,
                shape = RoundedCornerShape(LessTheme.radius.medium) // 16dp
            )
            .clickable(onClick = onClick)
            .padding(horizontal = LessTheme.spacing.medium, vertical = LessTheme.spacing.small),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading icon (optional, e.g., download icon for month filter)
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.size(LessTheme.spacing.xSmall))
            }
            
            // Text
            Text(
                text = text,
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            // Chevron down icon
            Spacer(modifier = Modifier.size(LessTheme.spacing.xSmall))
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_down24dp),
                contentDescription = null,
                modifier = Modifier.size(LessTheme.spacing.medium), // 16dp
                tint = LessTheme.colors.textIconsBlack
            )
        }
    }
}

