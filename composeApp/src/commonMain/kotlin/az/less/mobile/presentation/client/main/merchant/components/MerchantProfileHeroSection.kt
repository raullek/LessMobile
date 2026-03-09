package az.less.mobile.presentation.client.main.merchant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_left_24dp
import lessmobile.composeapp.generated.resources.ic_saved_24dp
import lessmobile.composeapp.generated.resources.ill_box_placeholder
import lessmobile.composeapp.generated.resources.ill_venue_placeholder
import org.jetbrains.compose.resources.painterResource

/**
 * Hero section with image, back button, favorite button, and merchant logo
 */
@Composable
fun MerchantProfileHeroSection(
    heroImageUrl: String?,
    merchantLogoUrl: String?,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        // Hero Image
        AsyncImage(
            model = heroImageUrl,
            contentDescription = "Merchant hero image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            error = painterResource(Res.drawable.ill_box_placeholder),
            placeholder = painterResource(Res.drawable.ill_box_placeholder)
        )

        // Back Button
        Box(
            modifier = Modifier
                .padding(start = LessTheme.spacing.medium, top = 48.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0x80171A1C))
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_left_24dp),
                contentDescription = "Back",
                tint = LessTheme.colors.backgroundPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        // Favorite Button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = LessTheme.spacing.medium, top = 48.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0x80171A1C))
                .clickable { onFavoriteClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_saved_24dp),
                contentDescription = "Favorite",
                tint = if (isFavorite) LessTheme.colors.textIconsBrand else LessTheme.colors.backgroundPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        // Merchant Logo (square with rounded corners)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(80.dp)
                .shadow(
                    elevation = LessTheme.elevation.medium,
                    shape = RoundedCornerShape(LessTheme.radius.medium)
                )
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.backgroundPrimary),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = merchantLogoUrl,
                contentDescription = "Merchant logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.medium)),
                error = painterResource(Res.drawable.ill_venue_placeholder),
                placeholder = painterResource(Res.drawable.ill_venue_placeholder)
            )
        }
    }
}
