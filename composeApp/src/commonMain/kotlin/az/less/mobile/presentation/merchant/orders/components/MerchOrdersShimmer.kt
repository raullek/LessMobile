package az.less.mobile.presentation.merchant.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import com.valentinilk.shimmer.shimmer

/**
 * Shimmer placeholder for Merchant Orders Screen.
 * Matches the MerchBoxCard layout: image, price, title, box type + pickup, button.
 */
@Composable
fun MerchOrdersShimmer(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .shimmer(),
        contentPadding = PaddingValues(
            start = LessTheme.spacing.medium,
            end = LessTheme.spacing.medium
        ),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        items(3) {
            MerchBoxCardShimmer()
        }
    }
}

@Composable
private fun MerchBoxCardShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.backgroundPrimary)
    ) {
        // Image placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(2.dp)
                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
                .padding(top = 14.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Price row
            Row(horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(LessTheme.colors.elementsSecondaryElement)
                )
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(LessTheme.colors.elementsSecondaryElement)
                )
            }

            // Title
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(LessTheme.colors.elementsSecondaryElement)
            )

            // Divider
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LessTheme.colors.backgroundSecond)
            )

            // Box type + pickup time
            Column(verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)) {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(LessTheme.colors.elementsSecondaryElement)
                )
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(LessTheme.colors.elementsSecondaryElement)
                )
            }
        }

        // Button placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
                .padding(bottom = LessTheme.spacing.small)
                .height(48.dp)
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.elementsSecondaryElement)
        )
    }
}
