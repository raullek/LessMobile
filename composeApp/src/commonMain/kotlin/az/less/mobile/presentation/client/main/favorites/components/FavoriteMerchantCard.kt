package az.less.mobile.presentation.client.main.favorites.components

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
import az.less.mobile.presentation.client.main.favorites.models.FavoriteMerchant
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_star_24dp
import lessmobile.composeapp.generated.resources.ill_box_placeholder
import lessmobile.composeapp.generated.resources.ill_venue_placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun FavoriteMerchantCard(
    merchant: FavoriteMerchant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = LessTheme.spacing.xSmall,
                shape = RoundedCornerShape(LessTheme.radius.medium),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.08f)
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
                    model = merchant.imageUrl,
                    contentDescription = merchant.merchantName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(Res.drawable.ill_box_placeholder),
                    placeholder = painterResource(Res.drawable.ill_box_placeholder)
                )
            }

            // Badge - top left
            val badgeLabel = merchant.badgeText
                ?: if (merchant.itemsOnSale > 0) "${merchant.itemsOnSale} items on sale"
                else "No active offer"

            Box(
                modifier = Modifier
                    .padding(start = LessTheme.spacing.small, top = LessTheme.spacing.small)
                    .align(Alignment.TopStart)
                    .background(
                        color = if (merchant.itemsOnSale > 0)
                            LessTheme.colors.textIconsBrand
                        else
                            LessTheme.colors.textIconsGrey,
                        shape = RoundedCornerShape(LessTheme.radius.xLarge)
                    )
                    .padding(
                        horizontal = LessTheme.spacing.xSmall,
                        vertical = LessTheme.spacing.xxSmall
                    )
            ) {
                Text(
                    text = badgeLabel,
                    style = LessTheme.typography.caption12Semibold,
                    color = LessTheme.colors.textIconsNested
                )
            }

            // Merchant logo - bottom right overlapping
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-LessTheme.spacing.small), y = 20.dp)
                    .size(LessTheme.size.large)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.backgroundPrimary)
                    .border(
                        width = 2.dp,
                        color = LessTheme.colors.backgroundPrimary,
                        shape = RoundedCornerShape(LessTheme.radius.small)
                    )
            ) {
                AsyncImage(
                    model = merchant.merchantLogoUrl,
                    contentDescription = "${merchant.merchantName} logo",
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
                .padding(horizontal = LessTheme.spacing.small)
                .padding(top = LessTheme.spacing.xxSmall, bottom = LessTheme.spacing.small)
        ) {
            Text(
                text = merchant.merchantName,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

            Text(
                text = merchant.address,
                style = LessTheme.typography.body14Medium,
                color = LessTheme.colors.textIconsGrey,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

            Row(
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                color = LessTheme.colors.textIconsWarning,
                                shape = RoundedCornerShape(LessTheme.radius.xSmall)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_star_24dp),
                            contentDescription = "Rating",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = merchant.rating.toString(),
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsBlack
                    )
                }

                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(
                            color = LessTheme.colors.textIconsBlack,
                            shape = CircleShape
                        )
                )

                Text(
                    text = merchant.distance,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }
    }
}
