package az.less.mobile.presentation.client.main.offers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_star_24dp
import lessmobile.composeapp.generated.resources.ill_box_placeholder
import lessmobile.composeapp.generated.resources.ill_venue_placeholder
import lessmobile.composeapp.generated.resources.orders_pickup_time
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Vertical offer card component for Top Rated and Top Picks sections
 * Based on Figma design with image, pricing, restaurant info, pickup time, rating, and distance
 */
@Composable
fun OfferCard(
    offerItem: OfferItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                spotColor = Color.Black.copy(alpha = 0.08f),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                shape = RoundedCornerShape(LessTheme.radius.medium)
            )
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.backgroundPrimary)
            .clickable { onClick() }
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
                    .padding(2.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(Color(0xFFFFF2EB))
            ) {
                    AsyncImage(
                        model = offerItem.imageUrl,
                        contentDescription = offerItem.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        error = painterResource(Res.drawable.ill_box_placeholder),
                        placeholder = painterResource(Res.drawable.ill_box_placeholder)
                    )
                }

            // "X left" badge - top left
            if (offerItem.itemsLeft > 0) {
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
                        text = "${offerItem.itemsLeft} left",
                        style = LessTheme.typography.caption12Semibold,
                        color = LessTheme.colors.textIconsNested
                    )
                }
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

                    AsyncImage(
                        model = offerItem.restaurantLogoUrl,
                        contentDescription = "${offerItem.restaurantName} logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        error = painterResource(Res.drawable.ill_venue_placeholder),
                        placeholder = painterResource(Res.drawable.ill_venue_placeholder)
                    )

            }
        }

        // Content section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 2.dp, bottom = 14.dp),
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
                        text = offerItem.originalPrice,
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
                        text = offerItem.currentPrice,
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

            // Title
            Text(
                text = offerItem.title,
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

            // Bag type, Category, and Pickup time
            Column(
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
            ) {
                // Bag type (green text)
                if (offerItem.bagType != null) {
                    Text(
                        text = offerItem.bagType,
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsBrand
                    )
                }

                // Category (black text)
                Text(
                    text = offerItem.category,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsBlack
                )

                // Pickup time (grey text)
                val pickupParts = offerItem.pickupTime.split(" - ")
                Text(
                    text = if (pickupParts.size == 2) {
                        stringResource(Res.string.orders_pickup_time, pickupParts[0], pickupParts[1])
                    } else {
                        offerItem.pickupTime
                    },
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
                        Icon(
                            painter = painterResource(Res.drawable.ic_star_24dp),
                            contentDescription = "Rating",
                            modifier = Modifier.size(16.dp),
                            tint = LessTheme.colors.backgroundPrimary
                        )
                    }

                    Text(
                        text = offerItem.rating.toString(),
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
                    text = offerItem.distance,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }
    }
}
