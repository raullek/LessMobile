package az.less.mobile.presentation.client.main.offers.components

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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.offers.models.OfferItem
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.compose_multiplatform
import lessmobile.composeapp.generated.resources.test_merchant_logo
import lessmobile.composeapp.generated.resources.test_offer_item_image
import org.jetbrains.compose.resources.painterResource

/**
 * Vertical offer card component for Top Rated and Top Picks sections
 * Based on Figma design with image, pricing, restaurant info, pickup time, rating, and distance
 */
@Composable
fun OfferCard(
    offerItem: az.less.mobile.presentation.client.main.offers.models.OfferItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(277.dp) // Specific card width from design
            .shadow(
                elevation = LessTheme.elevation.small,
                shape = RoundedCornerShape(LessTheme.radius.medium),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.elementsPrimaryElement)
            .clickable { onClick() }
            .padding(LessTheme.spacing.xxxSmall)
    ) {
        // Large Image with colored background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp) // Specific image height from design
                .clip(RoundedCornerShape(
                    topStart = LessTheme.spacing.small + LessTheme.spacing.xxxSmall, // 12 + 2 = 14dp
                    topEnd = LessTheme.spacing.small + LessTheme.spacing.xxxSmall
                ))
                .background(Color(0xFFFFF2EB)), // Parse imageBgColor if needed
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.test_offer_item_image),
                contentDescription = offerItem.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(width = 283.dp, height = 159.dp) // Specific image dimensions
            )
        }
        
        // Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LessTheme.spacing.small)
        ) {
            // Title
            Text(
                text = offerItem.title,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.small))
            
            // Pricing Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Original Price (gray with strikethrough)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = offerItem.originalPrice,
                        style = LessTheme.typography.caption12Semibold,
                        color = LessTheme.colors.textIconsGrey,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = " ₼",
                        style = LessTheme.typography.caption10Regular,
                        color = LessTheme.colors.textIconsGrey
                    )
                }
                
                Spacer(modifier = Modifier.width(LessTheme.spacing.xxSmall))
                
                // Current Price (brand color)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = offerItem.currentPrice,
                        style = LessTheme.typography.body16Semibold,
                        color = LessTheme.colors.textIconsBrand
                    )
                    Text(
                        text = " ₼",
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsBrand
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))

            }
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.medium + LessTheme.spacing.xxxSmall)) // 16 + 2 = 18dp

            // Restaurant Logo
            Box(
                modifier = Modifier
                    .size(LessTheme.size.large)
                    .clip(RoundedCornerShape(LessTheme.radius.xSmall))
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(Res.drawable.test_merchant_logo),
                    contentDescription = "Restaurant Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(LessTheme.size.large)
                )
            }
            
            // Restaurant Name (Brand colored)
            Text(
                text = offerItem.restaurantName,
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsBrand
            )
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall + LessTheme.spacing.xxxSmall)) // 4 + 2 = 6dp
            
            // Pickup Time
            Text(
                text = offerItem.pickupTime,
                style = LessTheme.typography.body14Medium,
                color = LessTheme.colors.textIconsGrey,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.small))
            
            // Divider
            Divider(
                color = LessTheme.colors.borderPrimary,
                thickness = 1.dp
            )
            
            Spacer(modifier = Modifier.height(LessTheme.spacing.small))
            
            // Rating and Distance Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall + LessTheme.spacing.xxxSmall) // 4 + 2 = 6dp
            ) {
                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
                ) {
                    // Star icon with orange background
                    Box(
                        modifier = Modifier
                            .size(LessTheme.size.small)
                            .clip(RoundedCornerShape(LessTheme.radius.xSmall))
                            .background(LessTheme.colors.textIconsWarning)
                            .padding(LessTheme.spacing.xxxSmall),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.compose_multiplatform),
                            contentDescription = "Rating",
                            tint = Color.White,
                            modifier = Modifier.size(LessTheme.size.xSmall)
                        )
                    }
                    
                    Text(
                        text = offerItem.rating.toString(),
                        style = LessTheme.typography.body14Semibold,
                        color = LessTheme.colors.textIconsBlack
                    )
                }
                
                // Dot Separator
                Box(
                    modifier = Modifier
                        .size(3.dp) // Specific dot size
                        .clip(CircleShape)
                        .background(LessTheme.colors.textIconsBlack)
                )
                
                // Distance
                Text(
                    text = offerItem.distance,
                    style = LessTheme.typography.body14Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }
    }
}

