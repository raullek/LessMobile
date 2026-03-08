package az.less.mobile.presentation.client.main.orders.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.orders.models.Order
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.cd_completed
import lessmobile.composeapp.generated.resources.ic_check_rounded_36dp
import lessmobile.composeapp.generated.resources.orders_picked_up_on
import lessmobile.composeapp.generated.resources.orders_pickup_time
import lessmobile.composeapp.generated.resources.orders_reserve_number
import lessmobile.composeapp.generated.resources.test_offer_item_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

/**
 * Stateless OrderItem component for displaying cart/history items
 *
 * @param item Order data to display
 * @param onClick Callback when item is clicked
 * @param modifier Modifier to be applied to the component
 */
@Composable
fun OrderItem(
    item: Order,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(71.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium),
    ) {
        // Image - 71x71dp, rounded 12dp
        Box(
            modifier = Modifier
                .size(71.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsThirdElement)
        ) {
            if (item.imageUrl != null) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(Res.drawable.test_offer_item_image),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Dark overlay and checkmark for completed orders
            if (item.isCompleted) {
                // Dark overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                )

                // Checkmark icon
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_check_rounded_36dp),
                    contentDescription = stringResource(Res.string.cd_completed),
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center)
                )
            }
        }

        // Content section
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium),
        ) {
            // Text content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
            ) {
                // Title
                Text(
                    text = item.title,
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Reserve number with styled text
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = LessTheme.colors.textIconsGrey)) {
                            append(stringResource(Res.string.orders_reserve_number))
                        }
                        withStyle(style = SpanStyle(color = LessTheme.colors.textIconsBlack)) {
                            append(item.reserveNumber)
                        }
                    },
                    style = LessTheme.typography.body14Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Pickup time or completed date
                if (item.isCompleted && item.completedDate != null) {
                    Text(
                        text = stringResource(Res.string.orders_picked_up_on, item.completedDate ?: ""),
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsBrand,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else if (item.pickupTimeStart != null && item.pickupTimeEnd != null) {
                    Text(
                        text = stringResource(Res.string.orders_pickup_time, item.pickupTimeStart, item.pickupTimeEnd),
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsGrey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Price section
            Row(
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = item.price,
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBrand
                )
                Text(
                    text = "\u20BC",
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBrand
                )
            }
        }
    }
}
