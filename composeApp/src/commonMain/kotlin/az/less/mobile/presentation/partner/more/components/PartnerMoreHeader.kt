package az.less.mobile.presentation.partner.more.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import coil3.compose.AsyncImage

@Composable
fun PartnerMoreHeader(
    venueName: String,
    modifier: Modifier = Modifier,
    venueLogoUrl: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.xLarge, vertical = LessTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(LessTheme.colors.elementsSecondaryElement),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = venueLogoUrl,
                contentDescription = "$venueName logo",
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = venueName,
            style = LessTheme.typography.title28Bold,
            color = LessTheme.colors.textIconsBlack
        )
    }
}
