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
import androidx.compose.ui.unit.sp
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
 * Based on Figma design: node-id=2121-38423
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
            // Main image with 2dp outer padding, 14dp top corners
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    error = painterResource(Res.drawable.ill_box_placeholder),
                    placeholder = painterResource(Res.drawable.ill_box_placeholder)
                )
            }

            // "X left" badge - top left, px=8 py=4
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

            // Merchant logo - bottom right overlapping, 40dp, rounded 12.5dp
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

        // Content section - px=12, pt=2, pb=14, gap=10
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 2.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Price + Title + Venue name (grouped together per Figma)
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Price row - gap=4 between original and discounted
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Original price (strikethrough, 13sp per Figma)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = offerItem.originalPrice,
                            style = LessTheme.typography.body14Semibold.copy(
                                fontSize = 13.sp,
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

                    // Discounted price (16sp semibold, green)
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

                // Venue name
                if (offerItem.restaurantName.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = LessTheme.spacing.xSmall),
                        text = offerItem.restaurantName,
                        style = LessTheme.typography.body16Bold,
                        color = LessTheme.colors.textIconsBlack,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Divider
            HorizontalDivider(
                color = LessTheme.colors.backgroundSecond,
                thickness = 1.dp
            )

            // Bag type, Category, and Pickup time - gap=4
            Column(
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
            ) {
                // Bag type (green text, 14sp medium)
                if (offerItem.bagType != null) {
                    Text(
                        text = offerItem.bagType,
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsBrand,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Category (black text, 14sp medium)
                Text(
                    text = offerItem.category,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Pickup time (grey text, 14sp medium)
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

            // Rating and distance row - gap=6
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rating - star icon in 20dp green rounded box + text
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Star icon box - 20dp, rounded 8dp, 2dp padding
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                color = LessTheme.colors.textIconsBrand,
                                shape = RoundedCornerShape(8.dp)
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

                // Dot separator - 3dp circle
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
