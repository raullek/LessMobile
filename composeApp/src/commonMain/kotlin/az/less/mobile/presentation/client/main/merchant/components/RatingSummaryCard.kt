package az.less.mobile.presentation.client.main.merchant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import az.less.designsystem.base.LessTheme
import az.less.mobile.utils.formatOneDecimal
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_star_16dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun RatingSummaryCard(
    averageRating: Float,
    totalReviews: Int,
    distribution: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val maxCount = distribution.values.maxOrNull() ?: 1

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                LessTheme.colors.backgroundPrimary,
                RoundedCornerShape(LessTheme.radius.small)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Distribution bars
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (star in 5 downTo 1) {
                val count = distribution[star] ?: 0
                val fraction = if (maxCount > 0) count.toFloat() / maxCount else 0f

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$star",
                        style = LessTheme.typography.caption12Regular,
                        color = LessTheme.colors.textIconsBlack,
                        modifier = Modifier.width(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        painter = painterResource(Res.drawable.ic_star_16dp),
                        contentDescription = null,
                        tint = LessTheme.colors.textIconsWarning,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(LessTheme.colors.borderPrimary)
                    ) {
                        if (fraction > 0f) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF006D60))
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        // Right: Average score + stars + total
        Column(
            modifier = Modifier.width(96.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = averageRating.formatOneDecimal(),
                style = LessTheme.typography.body16Semibold.copy(fontSize = 40.sp),
                color = LessTheme.colors.textIconsBlack
            )

            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(5) { index ->
                    Icon(
                        painter = painterResource(Res.drawable.ic_star_16dp),
                        contentDescription = null,
                        tint = if (index < averageRating.toInt())
                            LessTheme.colors.textIconsWarning
                        else
                            LessTheme.colors.borderPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$totalReviews Reviews",
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsGrey
            )
        }
    }
}
