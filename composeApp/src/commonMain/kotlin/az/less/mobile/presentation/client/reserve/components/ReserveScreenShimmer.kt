package az.less.mobile.presentation.client.reserve.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun ReserveScreenShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
            .background(color = LessTheme.colors.backgroundSecond)
            .fillMaxWidth()
            .padding(LessTheme.spacing.medium)
            .shimmer()
    ) {
        // Lot name
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(LessTheme.colors.textIconsSecondary)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        // Pickup time
        Box(
            modifier = Modifier
                .width(220.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(LessTheme.colors.textIconsSecondary)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        // Description
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(LessTheme.colors.textIconsSecondary)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Quantity selector area
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.medium))
                    .background(LessTheme.colors.textIconsSecondary)
            )
            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )
            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.medium))
                    .background(LessTheme.colors.textIconsSecondary)
            )
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.small))

        // Items left text
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(LessTheme.colors.textIconsSecondary)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Venue row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.textIconsSecondary)
            )
            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
                Spacer(modifier = Modifier.height(LessTheme.spacing.xxxSmall))
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
            }
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Voucher row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.textIconsSecondary)
            )
            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Price rows
        repeat(2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
            }
            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.small))

        // Payment method box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.textIconsSecondary)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Reserve button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.textIconsSecondary)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
    }
}
