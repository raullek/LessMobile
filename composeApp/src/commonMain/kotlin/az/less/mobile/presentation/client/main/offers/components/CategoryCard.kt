package az.less.mobile.presentation.client.main.offers.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Category card component with image and title
 * Based on Figma design: White card with food image (64px height) and category name
 */
@Composable
fun CategoryCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    //Temporary for testing
    testImage: DrawableResource
) {
    Column(
        modifier = modifier
            .width(81.dp) // Fixed width to match image width and ensure consistency
            .height(102.dp)
            .clip(RoundedCornerShape(LessTheme.radius.small))
            .background(LessTheme.colors.elementsPrimaryElement)
            .clickable { onClick() }
            .padding(
                top = LessTheme.spacing.xxSmall,
                bottom = LessTheme.spacing.small + LessTheme.spacing.xxxSmall // 12 + 2 = 14dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Container
        Box(
            modifier = Modifier
                .size(width = 81.dp, height = LessTheme.size.huge), // Keep width as design-specific, height 64dp
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(testImage),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(LessTheme.size.huge)
            )
        }
        
        // Category Title
        Text(
            text = title,
            style = LessTheme.typography.body14Semibold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )
    }
}

