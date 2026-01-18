package az.less.mobile.presentation.client.main.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import com.valentinilk.shimmer.shimmer

/**
 * Shimmer placeholder for Orders Screen
 * Displays loading state that matches the OrderItem layout
 */
@Composable
fun OrdersScreenShimmer(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .shimmer(),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
    ) {
        items(5) { index ->
            OrderItemShimmer()

            // Divider between items (except last)
            if (index < 4) {
                HorizontalDivider(
                    color = LessTheme.colors.borderPrimary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = LessTheme.spacing.medium)
                )
            }
        }
    }
}

/**
 * Shimmer placeholder for OrderItem
 * Matches the layout: image (71dp) + content section with title, reserve number, pickup time, price
 */
@Composable
private fun OrderItemShimmer(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(71.dp),
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        // Image placeholder - 71x71dp
        Box(
            modifier = Modifier
                .size(71.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.textIconsSecondary)
        )

        // Content section
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
        ) {
            // Text content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
            ) {
                // Title placeholder
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )

                // Reserve number placeholder
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )

                // Pickup time placeholder
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
            }

            // Price placeholder
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(LessTheme.colors.textIconsSecondary)
                    .align(Alignment.Top)
            )
        }
    }
}
