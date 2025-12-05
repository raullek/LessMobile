package az.less.mobile.presentation.main.categoryoffers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_left_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Header component for Category Offers screen with back button and title
 * Based on Search screen header design
 */
@Composable
fun CategoryOffersHeader(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(LessTheme.colors.elementsPrimaryElement)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_left_24dp),
                contentDescription = "Back",
                tint = LessTheme.colors.textIconsBrand,
                modifier = Modifier.size(LessTheme.size.medium)
            )
        }
        
        Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
        
        // Title
        Text(
            text = title,
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsBlack,
            modifier = Modifier.weight(1f)
        )
        
        // Spacer to balance the layout (same width as back button)
        Spacer(modifier = Modifier.width(LessTheme.size.xLarge + LessTheme.spacing.xxSmall))
    }
}

