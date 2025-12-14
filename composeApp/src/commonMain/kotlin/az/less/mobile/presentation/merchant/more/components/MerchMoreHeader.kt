package az.less.mobile.presentation.merchant.more.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.compose_multiplatform
import lessmobile.composeapp.generated.resources.ic_star_16dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Header component for MerchMore screen
 * Shows merchant image, name, email, and rating
 */
@Composable
fun MerchMoreHeader(
    merchantName: String,
    merchantEmail: String,
    rating: String,
    reviewCount: String,
    modifier: Modifier = Modifier,
    merchantImageRes: DrawableResource? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.xLarge, vertical = LessTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        // Merchant image
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(LessTheme.colors.elementsSecondaryElement),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(merchantImageRes ?: Res.drawable.compose_multiplatform),
                contentDescription = "Merchant Image",
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Merchant name and email
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall)
        ) {
            Text(
                text = merchantName,
                style = LessTheme.typography.title28Bold,
                color = LessTheme.colors.textIconsBlack
            )

            Text(
                text = merchantEmail,
                style = LessTheme.typography.body16Regular,
                color = LessTheme.colors.textIconsGrey
            )
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))

        // Rating badge
        RatingBadge(
            rating = rating,
            reviewCount = reviewCount
        )
    }
}

/**
 * Rating badge component showing star icon, rating value, and review count
 */
@Composable
private fun RatingBadge(
    rating: String,
    reviewCount: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(LessTheme.colors.backgroundSecond)
            .padding(LessTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall)
    ) {
        // Star icon with orange background
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(LessTheme.colors.textIconsWarning),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_star_16dp),
                contentDescription = null,
                tint = LessTheme.colors.textIconsLightBrand,
                modifier = Modifier.size(16.dp)
            )
        }

        // Rating text
        Row(
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rating,
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsBlack
            )

            Text(
                text = "($reviewCount)",
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsGrey
            )
        }
    }
}
