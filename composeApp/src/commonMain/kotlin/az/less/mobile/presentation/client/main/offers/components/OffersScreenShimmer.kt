package az.less.mobile.presentation.client.main.offers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import com.valentinilk.shimmer.shimmer

/**
 * Optimized shimmer placeholder for Offers Screen
 * Uses simple Column/Row instead of Lazy for faster initial render
 */
@Composable
fun OffersScreenShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .shimmer()
    ) {
        // Categories Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = LessTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
        ) {
            repeat(5) {
                Box(
                    modifier = Modifier
                        .width(81.dp)
                        .height(102.dp)
                        .clip(RoundedCornerShape(LessTheme.radius.small))
                        .background(LessTheme.colors.textIconsSecondary)
                )
            }
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))

        // Special Discount Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium)
                .height(173.dp)
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.textIconsSecondary)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Filter Categories Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(LessTheme.radius.small))
                        .background(LessTheme.colors.textIconsSecondary)
                )
            }
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Offer Sections (2 sections)
        repeat(2) {
            // Section Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LessTheme.spacing.medium),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            // Offer Cards Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = LessTheme.spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
            ) {
                repeat(2) {
                    OfferCardShimmer()
                }
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
        }
    }
}

/**
 * Simplified OfferCard shimmer - just image and content block
 */
@Composable
private fun OfferCardShimmer() {
    Column(
        modifier = Modifier
            .width(277.dp)
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.backgroundPrimary)
    ) {
        // Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                .background(LessTheme.colors.textIconsSecondary)
        )

        // Content placeholder
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LessTheme.colors.backgroundSecond)
            )
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(14.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )
        }
    }
}
