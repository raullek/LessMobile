package az.less.mobile.presentation.client.main.saved.components

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
import androidx.compose.foundation.layout.size
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
 * Shimmer placeholder for Saved Screen
 * Displays loading state that matches the SavedMerchantCard layout
 */
@Composable
fun SavedScreenShimmer(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .shimmer(),
        contentPadding = PaddingValues(
            horizontal = LessTheme.spacing.medium
        ),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        items(4) {
            SavedMerchantCardShimmer()
        }
    }
}

/**
 * Shimmer placeholder for SavedMerchantCard
 * Matches the layout: image (140dp) + content section
 */
@Composable
private fun SavedMerchantCardShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.backgroundPrimary)
    ) {
        // Image section placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(2.dp)
                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                .background(LessTheme.colors.textIconsSecondary)
        )

        // Content section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
                .padding(top = LessTheme.spacing.xxSmall, bottom = LessTheme.spacing.small)
        ) {
            // Merchant name placeholder
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

            // Address placeholder
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(14.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

            // Rating and distance row placeholder
            Row(
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
            ) {
                // Rating box
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(LessTheme.radius.xSmall))
                        .background(LessTheme.colors.textIconsSecondary)
                )
                // Rating text
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
                // Distance
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
            }
        }
    }
}
