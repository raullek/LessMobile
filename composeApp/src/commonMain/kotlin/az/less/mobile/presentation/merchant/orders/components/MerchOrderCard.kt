package az.less.mobile.presentation.merchant.orders.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.presentation.merchant.orders.model.MerchOrderItem
import az.less.mobile.presentation.merchant.orders.model.OrderButtonState
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_star_24dp
import lessmobile.composeapp.generated.resources.test_offer_item_image
import org.jetbrains.compose.resources.painterResource

/**
 * Order card component for merchant orders screen
 * Based on Figma design with different button states
 * 
 * @param order The order data to display
 * @param onCardClick Callback when card is clicked
 * @param onButtonClick Callback when action button is clicked
 * @param modifier Modifier to apply to the card
 */
@Composable
fun MerchOrderCard(
    order: MerchOrderItem,
    onCardClick: () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                spotColor = Color.Black.copy(alpha = 0.08f),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                shape = RoundedCornerShape(LessTheme.radius.medium) // 16dp
            )
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.backgroundPrimary)
            .clickable { onCardClick() }
    ) {
        // Image section with badge and logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            // Main image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 2.dp, vertical = 2.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(Color(0xFFFFF2EB))
            ) {
                if (order.imageUrl != null) {
                    AsyncImage(
                        model = order.imageUrl,
                        contentDescription = order.merchantName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Placeholder image
                    Image(
                        painter = painterResource(Res.drawable.test_offer_item_image),
                        contentDescription = order.merchantName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            
            // "X left" badge - top left
            Box(
                modifier = Modifier
                    .padding(start = 14.dp, top = 14.dp)
                    .align(Alignment.TopStart)
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(1000.dp)
                    )
                    .padding(horizontal = LessTheme.spacing.xSmall, vertical = LessTheme.spacing.xxSmall)
            ) {
                Text(
                    text = "${order.itemsLeft} left",
                    style = LessTheme.typography.caption12Semibold,
                    color = LessTheme.colors.textIconsNested
                )
            }
            
            // Merchant logo - bottom right overlapping
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-12).dp, y = 20.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.5.dp))
                    .background(LessTheme.colors.backgroundPrimary)
                    .border(
                        width = 2.dp,
                        color = LessTheme.colors.backgroundPrimary,
                        shape = RoundedCornerShape(12.5.dp)
                    )
            ) {
                if (order.merchantLogoUrl != null) {
                    AsyncImage(
                        model = order.merchantLogoUrl,
                        contentDescription = "${order.merchantName} logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Placeholder logo - green background
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LessTheme.colors.elementsPrimaryBrand),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = order.merchantName.firstOrNull()?.toString() ?: "M",
                            style = LessTheme.typography.body16Bold,
                            color = LessTheme.colors.textIconsNested
                        )
                    }
                }
            }
        }
        
        // Content section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Price row
            Row(
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Original price (strikethrough)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = order.originalPrice,
                        style = LessTheme.typography.body14Semibold.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = LessTheme.colors.textIconsGrey
                    )
                    Text(
                        text = "₼",
                        style = LessTheme.typography.caption12Semibold.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = LessTheme.colors.textIconsGrey
                    )
                }
                
                // Discounted price
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = order.discountedPrice,
                        style = LessTheme.typography.body16Semibold,
                        color = LessTheme.colors.textIconsBrand
                    )
                    Text(
                        text = "₼",
                        style = LessTheme.typography.body14Semibold,
                        color = LessTheme.colors.textIconsBrand
                    )
                }
            }
            
            // Merchant name
            Text(
                text = order.merchantName,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            // Divider
            HorizontalDivider(
                color = LessTheme.colors.backgroundSecond,
                thickness = 1.dp
            )
            
            // Product info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Left side - product name and pickup time
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
                ) {
                    // Product name (green)
                    Text(
                        text = order.productName,
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsBrand,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // Pickup time
                    Text(
                        text = "Pick up from ${order.pickupTimeStart} to ${order.pickupTimeEnd}",
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsGrey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Order number badge
                Box(
                    modifier = Modifier
                        .background(
                            color = LessTheme.colors.backgroundSecond,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = LessTheme.spacing.xxSmall)
                ) {
                    Text(
                        text = order.orderNumber,
                        style = LessTheme.typography.body16Medium,
                        color = LessTheme.colors.textIconsBlack
                    )
                }
            }
            
            // Divider
            HorizontalDivider(
                color = LessTheme.colors.backgroundSecond,
                thickness = 1.dp
            )
            
            // Rating and distance row
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Star icon
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                color = LessTheme.colors.textIconsBrand,
                                shape = RoundedCornerShape(LessTheme.spacing.xSmall)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_star_24dp),
                            contentDescription = "Rating",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    
                    Text(
                        text = order.rating,
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsBlack
                    )
                }
                
                // Dot separator
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(
                            color = LessTheme.colors.textIconsBlack,
                            shape = CircleShape
                        )
                )
                
                // Distance
                Text(
                    text = order.distance,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }
        
        // Action button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
                .padding(bottom = LessTheme.spacing.small)
        ) {
            OrderActionButton(
                buttonState = order.buttonState,
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Action button component with different states
 * 
 * Button states:
 * - HANDED_OVER: Green button "Handed Over" - marks order as delivered
 * - CANCEL_LOT: Red/destructive button "Cancel Lot" - cancels the order
 * - CANCELLATION_TIME_ENDED: Disabled grey button "Cancellation Time Ended"
 */
@Composable
private fun OrderActionButton(
    buttonState: OrderButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (buttonState) {
        OrderButtonState.HANDED_OVER -> {
            DsButton(
                text = "Handed Over",
                onClick = onClick,
                modifier = modifier,
                variant = ButtonVariant.Primary,
                size = ButtonSize.Medium
            )
        }
        
        OrderButtonState.CANCEL_LOT -> {
            DsButton(
                text = "Cancel Lot",
                onClick = onClick,
                modifier = modifier,
                variant = ButtonVariant.Secondary,
                size = ButtonSize.Medium,
                textColor = LessTheme.colors.textIconsError
            )
        }
        
        OrderButtonState.CANCELLATION_TIME_ENDED -> {
            DsButton(
                text = "Cancellation Time Ended",
                onClick = { /* Disabled - no action */ },
                modifier = modifier,
                variant = ButtonVariant.Secondary,
                size = ButtonSize.Medium,
                enabled = false
            )
        }
    }
}

