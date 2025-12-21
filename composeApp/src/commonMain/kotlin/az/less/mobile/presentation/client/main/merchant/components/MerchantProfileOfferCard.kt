package az.less.mobile.presentation.client.main.merchant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.merchant.models.MerchantOfferItem
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_star_16dp
import lessmobile.composeapp.generated.resources.test_merchant_logo
import lessmobile.composeapp.generated.resources.test_offer_item_image
import org.jetbrains.compose.resources.painterResource

/**
 * Merchant offer card with badge showing items on sale
 */
@Composable
fun MerchantProfileOfferCard(
    offer: MerchantOfferItem,
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
    ) {
        // Image with badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.test_offer_item_image),
                contentDescription = "Offer image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = LessTheme.radius.medium, topEnd = LessTheme.radius.medium))
            )

            // Badge
            Box(
                modifier = Modifier
                    .padding(LessTheme.spacing.small)
                    .clip(RoundedCornerShape(LessTheme.radius.xSmall))
                    .background(
                        if (offer.hasActiveDiscount)
                            LessTheme.colors.textIconsBrand
                        else
                            LessTheme.colors.textIconsGrey
                    )
                    .padding(horizontal = LessTheme.spacing.xSmall, vertical = LessTheme.spacing.xxxSmall)
            ) {
                Text(
                    text = if (offer.hasActiveDiscount)
                        "${offer.itemsOnSale} items on sale"
                    else
                        "No active discount",
                    style = LessTheme.typography.caption12Semibold,
                    color = LessTheme.colors.backgroundPrimary
                )
            }

            // Merchant Logo
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(LessTheme.spacing.small)
                    .size(40.dp)
                    .shadow(
                        elevation = LessTheme.elevation.small,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(LessTheme.colors.backgroundPrimary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.test_merchant_logo),
                    contentDescription = "Merchant logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LessTheme.spacing.medium)
        ) {
            // Merchant Name
            Text(
                text = offer.merchantName,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))

            // Pickup Time
            Text(
                text = offer.pickupTime,
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsGrey
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

            // Rating and Distance
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_star_16dp),
                    contentDescription = "Rating",
                    tint = LessTheme.colors.textIconsWarning,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(LessTheme.spacing.xxSmall))
                Text(
                    text = offer.rating.toString(),
                    style = LessTheme.typography.body14Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
                Text(
                    text = " \u2022 ",
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsBlack
                )
                Text(
                    text = offer.distance,
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }
    }
}
