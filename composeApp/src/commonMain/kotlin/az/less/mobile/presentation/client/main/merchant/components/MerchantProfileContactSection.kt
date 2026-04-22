package az.less.mobile.presentation.client.main.merchant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import lessmobile.composeapp.generated.resources.ic_map_24dp
import lessmobile.composeapp.generated.resources.ic_phone_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Contact section with phone and location
 */
@Composable
fun MerchantProfileContactSection(
    phoneNumber: String,
    address: String,
    onPhoneClick: () -> Unit,
    onViewLocationClick: () -> Unit,
    onDirectionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium)
    ) {
        // Phone Number Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPhoneClick() }
                .padding(vertical = LessTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Phone Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.backgroundSecond),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_phone_24dp),
                    contentDescription = "Phone",
                    tint = LessTheme.colors.textIconsBlack,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))

            Column {
                Text(
                    text = "Phone number",
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
                Text(
                    text = phoneNumber,
                    style = LessTheme.typography.body16Regular,
                    color = LessTheme.colors.textIconsGrey
                )
            }
        }

        // Location Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewLocationClick() }
                .padding(vertical = LessTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.backgroundSecond),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_map_24dp),
                    contentDescription = "Location",
                    tint = LessTheme.colors.textIconsBlack,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "View location",
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
                Text(
                    text = address,
                    style = LessTheme.typography.body16Regular,
                    color = LessTheme.colors.textIconsGrey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Directions link
            Row(
                modifier = Modifier.clickable { onDirectionsClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Directions",
                    style = LessTheme.typography.body14Semibold,
                    color = LessTheme.colors.textIconsBrand
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_chevron_right_24dp),
                    contentDescription = "Directions",
                    tint = LessTheme.colors.textIconsBrand,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
    }
}
