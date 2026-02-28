package az.less.mobile.presentation.client.main.merchant.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import az.less.designsystem.base.LessTheme
import az.less.mobile.domain.model.MerchantReview
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_star_16dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun MerchantReviewCard(
    review: MerchantReview,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                LessTheme.colors.backgroundPrimary,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        // Header: Avatar + (Stars/Date + Name)
        Row {
            // Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(LessTheme.colors.backgroundSecond),
                contentAlignment = Alignment.Center
            ) {
                if (review.clientAvatar != null) {
                    AsyncImage(
                        model = review.clientAvatar,
                        contentDescription = review.clientName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                    )
                } else {
                    Text(
                        text = review.clientName.take(1).uppercase(),
                        style = LessTheme.typography.body16Semibold,
                        color = LessTheme.colors.textIconsGrey
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right content: Stars + Date row, then Name
            Column(modifier = Modifier.weight(1f)) {
                // Stars + Date row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stars
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(5) { index ->
                            Icon(
                                painter = painterResource(Res.drawable.ic_star_16dp),
                                contentDescription = null,
                                tint = if (index < review.rating)
                                    LessTheme.colors.textIconsWarning
                                else
                                    LessTheme.colors.borderPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Date
                    Text(
                        text = formatReviewDate(review.createdAt),
                        style = LessTheme.typography.body14Regular,
                        color = Color(0x59131313)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Name
                Text(
                    text = review.clientName,
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }

        // Comment
        if (review.comment.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.comment,
                style = LessTheme.typography.body14Regular.copy(lineHeight = 18.sp),
                color = LessTheme.colors.textIconsGrey
            )
        }
    }
}

private fun formatReviewDate(isoString: String): String {
    if (isoString.isEmpty()) return ""
    val datePart = isoString.substringBefore("T")
    val parts = datePart.split("-")
    if (parts.size != 3) return datePart
    val year = parts[0]
    val month = parts[1].toIntOrNull() ?: return datePart
    val day = parts[2].toIntOrNull() ?: return datePart
    val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val monthName = monthNames.getOrElse(month - 1) { return datePart }
    return "$day $monthName $year"
}
