package az.less.mobile.presentation.client.main.merchant.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun MerchantProfileShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .shimmer()
    ) {
        // Hero image area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(LessTheme.colors.textIconsSecondary)
        )

        // Logo placeholder (centered, overlapping hero)
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 0.dp) // logo overlaps but shimmer is flat
                .size(80.dp)
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.backgroundPrimary)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Name
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(LessTheme.colors.textIconsSecondary)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        // Description line 1
        Box(
            modifier = Modifier
                .width(260.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(LessTheme.colors.textIconsSecondary)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))

        // Description line 2
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(LessTheme.colors.textIconsSecondary)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Rating pill
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.textIconsSecondary)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Phone row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium, vertical = LessTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.textIconsSecondary)
            )
            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
            Column {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
                Spacer(modifier = Modifier.height(LessTheme.spacing.xxxSmall))
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
            }
        }

        // Location row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium, vertical = LessTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.textIconsSecondary)
            )
            Spacer(modifier = Modifier.width(LessTheme.spacing.medium))
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
                Spacer(modifier = Modifier.height(LessTheme.spacing.xxxSmall))
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LessTheme.colors.textIconsSecondary)
                )
            }
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Tabs placeholder
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(LessTheme.colors.textIconsSecondary)
            )
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        // Tab indicator line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LessTheme.colors.textIconsSecondary)
        )
    }
}
