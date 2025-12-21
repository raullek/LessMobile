package az.less.mobile.presentation.merchant.places.edit.components

import androidx.compose.foundation.Image
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
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_left_24dp
import lessmobile.composeapp.generated.resources.ic_edit_24dp
import lessmobile.composeapp.generated.resources.image_placeholder
import lessmobile.composeapp.generated.resources.test_merchant_logo
import org.jetbrains.compose.resources.painterResource

/**
 * Hero section with image, back button, edit icon, and merchant logo
 * Based on MerchantProfileHeroSection but with edit icon instead of favorite
 */
@Composable
fun EditMerchantProfileHeroSection(
    venueImageUrl: String?,
    logoUrl: String?,
    onBackClick: () -> Unit,
    onVenueImageEditClick: () -> Unit,
    onLogoEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        // Hero Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(LessTheme.colors.elementsThirdElement)
        ) {
            if (venueImageUrl != null) {
                Image(
                    painter = painterResource(Res.drawable.image_placeholder),
                    contentDescription = "Venue image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                // Show placeholder text when no image
                Image(
                    painter = painterResource(Res.drawable.image_placeholder),
                    contentDescription = "Venue image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Edit icon overlay for venue image
            IconButton(
                onClick = onVenueImageEditClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(LessTheme.spacing.medium)
                    .clip(CircleShape)
                    .background(Color(0x80171A1C))

            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit_24dp),
                    contentDescription = "Edit venue image",
                    tint = LessTheme.colors.textIconsNested
                )
            }
        }

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
                tint = LessTheme.colors.textIconsNested,
                modifier = Modifier.size(24.dp)
            )
        }

        // Merchant Logo (square with rounded corners) with edit icon
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
            if (logoUrl != null) {
                Image(
                    painter = painterResource(Res.drawable.test_merchant_logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(LessTheme.radius.medium))
                )
            } else {
                // Show placeholder text when no logo
                Image(
                    painter = painterResource(Res.drawable.image_placeholder),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(LessTheme.radius.medium))
                )
            }

            // Edit icon overlay for logo
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0x80171A1C))
                    .clickable { onLogoEditClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit_24dp),
                    contentDescription = "Edit logo",
                    tint = LessTheme.colors.textIconsNested,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

