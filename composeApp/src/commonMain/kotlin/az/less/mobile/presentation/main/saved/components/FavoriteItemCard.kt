package az.less.mobile.presentation.main.saved.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.main.saved.models.FavoriteItem
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.compose_multiplatform
import lessmobile.composeapp.generated.resources.test_merchant_logo
import lessmobile.composeapp.generated.resources.test_offer_item_image
import org.jetbrains.compose.resources.painterResource

/**
 * Favorite item card component for Saved screen
 * Displays a saved offer with image, price, pickup time, rating, and distance
 */
@Composable
fun FavoriteItemCard(
    item: FavoriteItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = LessTheme.elevation.small,
                shape = RoundedCornerShape(LessTheme.radius.medium),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.backgroundPrimary)
            .clickable { onClick() }
            .padding(LessTheme.spacing.xxxSmall)
    ) {
        // Image - 2dp from top and right, no bottom rounded corners
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp) // Specific height from design
                .clip(RoundedCornerShape(
                    topStart = LessTheme.radius.small,
                    topEnd = LessTheme.radius.small,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ))
        ) {
            Image(
                painter = painterResource(Res.drawable.test_offer_item_image),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.small))
        
        // Content section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = LessTheme.size.large + LessTheme.spacing.xxSmall) // Make room for merchant logo
            ) {
                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.price,
                        style = LessTheme.typography.body16Semibold,
                        color = LessTheme.colors.textIconsBrand
                    )
                    Text(
                        text = " ₼",
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsBrand
                    )
                }
                
                Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))
                
                // Title
                Text(
                    text = item.title,
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Restaurant Logo - positioned at top right, aligned with price level
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(LessTheme.size.large)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.test_merchant_logo),
                    contentDescription = "Restaurant Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(LessTheme.size.large)
                )
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
        ) {
            Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))
            
            // Pickup time
            Text(
                text = item.pickupTime,
                style = LessTheme.typography.body14Medium,
                color = LessTheme.colors.textIconsGrey,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))
            
            // Rating and Distance
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
            ) {
                // Rating with star icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.compose_multiplatform),
                        contentDescription = "Rating",
                        tint = LessTheme.colors.textIconsWarning,
                        modifier = Modifier.size(LessTheme.size.xSmall)
                    )
                    
                    Text(
                        text = "${item.rating} (${item.reviewCount})",
                        style = LessTheme.typography.body14Semibold,
                        color = LessTheme.colors.textIconsBlack
                    )
                }
                
                // Dot separator
                Text(
                    text = "•",
                    style = LessTheme.typography.body14Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
                
                // Distance
                Text(
                    text = item.distance,
                    style = LessTheme.typography.body14Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.small))
        }
    }
}

