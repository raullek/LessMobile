package az.less.mobile.presentation.merchant.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.domain.model.BoxType
import az.less.mobile.presentation.merchant.orders.model.BoughtBoxItem
import az.less.mobile.presentation.merchant.orders.model.CreatedBoxItem
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.merch_orders_handed_over
import lessmobile.composeapp.generated.resources.merch_orders_cancellation_ended
import lessmobile.composeapp.generated.resources.merch_orders_items_left
import org.jetbrains.compose.resources.stringResource

/**
 * Card for bought boxes (Purchased tab).
 * Layout matches Figma: image + badge, price, title, box type + pickup + reserve#, button.
 */
@Composable
fun BoughtBoxCard(
    item: BoughtBoxItem,
    onCardClick: () -> Unit,
    onHandedOverClick: () -> Unit,
    isDelivering: Boolean = false,
    modifier: Modifier = Modifier
) {
    MerchBoxCardContainer(
        imageUrl = item.imageUrl,
        imageContentDescription = item.boxTitle,
        availableItems = null,
        originalPrice = item.originalPrice,
        discountedPrice = item.discountedPrice,
        title = item.boxTitle,
        boxType = item.boxType,
        pickupTimeFormatted = item.pickupTimeFormatted,
        reserveNumber = item.reserveNumber,
        onCardClick = onCardClick,
        modifier = modifier
    ) {
        // Handed Over button
        DsButton(
            text = stringResource(Res.string.merch_orders_handed_over),
            onClick = onHandedOverClick,
            modifier = Modifier.fillMaxWidth(),
            variant = ButtonVariant.Primary,
            size = ButtonSize.Medium,
            isLoading = isDelivering
        )
    }
}

/**
 * Card for created boxes (Placed Lots tab).
 * Same layout as BoughtBoxCard but with cancel button / expired state.
 */
@Composable
fun CreatedBoxCard(
    item: CreatedBoxItem,
    onCardClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MerchBoxCardContainer(
        imageUrl = item.imageUrl,
        imageContentDescription = item.title,
        availableItems = item.availableItems,
        originalPrice = item.originalPrice,
        discountedPrice = item.discountedPrice,
        title = item.title,
        boxType = item.boxType,
        pickupTimeFormatted = item.pickupTimeFormatted,
        reserveNumber = null,
        onCardClick = onCardClick,
        modifier = modifier
    ) {
        if (item.isCancelEnabled) {
            // Active cancel: red text on light red surface/error background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.medium))
                    .background(LessTheme.colors.surfaceError)
                    .clickable { onCancelClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.cancelButtonText ?: "",
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsError
                )
            }
        } else {
            // Expired: black text, no background, with close time
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        Res.string.merch_orders_cancellation_ended,
                        item.closeTimeFormatted ?: ""
                    ),
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }
    }
}

/**
 * Shared card container matching Figma design.
 * Both Purchased and Placed Lots cards use identical layout,
 * only the bottom button slot differs.
 */
@Composable
private fun MerchBoxCardContainer(
    imageUrl: String?,
    imageContentDescription: String,
    availableItems: Int?,
    originalPrice: Double,
    discountedPrice: Double,
    title: String,
    boxType: String?,
    pickupTimeFormatted: String,
    reserveNumber: String?,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSlot: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                spotColor = Color.Black.copy(alpha = 0.08f),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                shape = RoundedCornerShape(LessTheme.radius.medium)
            )
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.backgroundPrimary)
            .clickable { onCardClick() }
    ) {
        // Image section with badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(Color(0xFFFFF2EB))
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = imageContentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // "X left" badge
            if (availableItems != null) {
                Box(
                    modifier = Modifier
                        .padding(start = 14.dp, top = 14.dp)
                        .align(Alignment.TopStart)
                        .background(
                            color = Color.Black.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(1000.dp)
                        )
                        .padding(
                            horizontal = LessTheme.spacing.xSmall,
                            vertical = LessTheme.spacing.xxSmall
                        )
                ) {
                    Text(
                        text = stringResource(Res.string.merch_orders_items_left, availableItems),
                        style = LessTheme.typography.caption12Semibold,
                        color = LessTheme.colors.textIconsNested
                    )
                }
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
                .padding(top = 14.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Price row
            PriceRow(originalPrice = originalPrice, discountedPrice = discountedPrice)

            // Title
            Text(
                text = title,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(color = LessTheme.colors.backgroundSecond, thickness = 1.dp)

            // Box type + pickup time + reserve number
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
            ) {
                // Box type + pickup time (left side, takes remaining space)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
                ) {
                    val boxTypeEnum = BoxType.fromApi(boxType)
                    if (boxTypeEnum != null) {
                        Text(
                            text = stringResource(boxTypeEnum.labelRes),
                            style = LessTheme.typography.body14Medium,
                            color = LessTheme.colors.textIconsBrand,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = pickupTimeFormatted,
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsGrey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Reserve number badge (right side)
                if (reserveNumber != null && reserveNumber.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = LessTheme.colors.backgroundSecond,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = LessTheme.spacing.xxSmall)
                    ) {
                        Text(
                            text = "#$reserveNumber",
                            style = LessTheme.typography.body16Medium,
                            color = LessTheme.colors.textIconsBlack
                        )
                    }
                }
            }
        }

        // Button slot
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
                .padding(bottom = LessTheme.spacing.small)
        ) {
            buttonSlot()
        }
    }
}

@Composable
private fun PriceRow(
    originalPrice: Double,
    discountedPrice: Double
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Original price (strikethrough, 13sp per design)
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "%.2f".format(originalPrice),
                style = LessTheme.typography.body14Semibold.copy(
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.LineThrough
                ),
                color = LessTheme.colors.textIconsGrey
            )
            Text(
                text = "₼",
                style = LessTheme.typography.caption12Semibold.copy(
                    textDecoration = TextDecoration.LineThrough
                ),
                color = LessTheme.colors.textIconsGrey
            )
        }

        // Discounted price (16sp, brand color)
        Row(
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "%.2f".format(discountedPrice),
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBrand
            )
            Text(
                text = "₼",
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsBrand
            )
        }
    }
}
