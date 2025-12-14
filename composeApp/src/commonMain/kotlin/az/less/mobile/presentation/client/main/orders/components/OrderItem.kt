package az.less.mobile.presentation.client.main.orders.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.orders.models.CartItem
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.test_offer_item_image
import org.jetbrains.compose.resources.painterResource

/**
 * Stateless OrderItem component for displaying cart/history items
 * Strictly follows Figma design specifications
 * 
 * Note: Component does NOT have horizontal padding - padding should be applied by parent
 * 
 * @param item CartItem data to display
 * @param onClick Callback when item is clicked
 * @param modifier Modifier to be applied to the component
 */
@Composable
fun OrderItem(
    item: az.less.mobile.presentation.client.main.orders.models.CartItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(71.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.large),
    ) {
        // Image - 71x71dp, rounded 12dp
        Box(
            modifier = Modifier
                .size(71.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsThirdElement)
        ) {
//            if (item.imageUrl != null) {
//                AsyncImage(
//                    model = item.imageUrl,
//                    contentDescription = item.title,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//            }

            Image(
                painter = painterResource(Res.drawable.test_offer_item_image),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Content section - flex-1 with gap-16
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
            ,
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.large),
        ) {
            // Text content - flex-1 with gap-6 between title and pickup time
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = LessTheme.spacing.xxSmall),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title
                Text(
                    text = item.title,
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey,
                    text = "Reserve number:${item.reserveNumber}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                
                // Pickup time - gap-6 from title
                Text(
                    text = item.pickupTime,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Price section - gap-4
            Row(
                modifier = Modifier.padding(top = LessTheme.spacing.xxSmall),
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.price,
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBrand
                )
                // Currency symbol
                Text(
                    text = "₼",
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBrand
                )
            }
        }
    }
}

