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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.presentation.merchant.orders.model.BoughtBoxItem
import az.less.mobile.presentation.merchant.orders.model.CreatedBoxItem
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.merch_orders_handed_over
import lessmobile.composeapp.generated.resources.merch_orders_cancellation_ended
import lessmobile.composeapp.generated.resources.merch_orders_items_left
import org.jetbrains.compose.resources.stringResource

/**
 * Card for bought boxes (awaiting pickup tab)
 */
@Composable
fun BoughtBoxCard(
    item: BoughtBoxItem,
    onCardClick: () -> Unit,
    onHandedOverClick: () -> Unit,
    modifier: Modifier = Modifier
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
        // Image section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(2.dp)
                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                .background(Color(0xFFFFF2EB))
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.boxTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Price row
            PriceRow(originalPrice = item.originalPrice, discountedPrice = item.discountedPrice)

            // Box title
            Text(
                text = item.boxTitle,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(color = LessTheme.colors.backgroundSecond, thickness = 1.dp)

            // Client info + reserve number
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Client info
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Client avatar
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(LessTheme.colors.elementsSecondaryElement),
                        contentAlignment = Alignment.Center
                    ) {
                        if (item.clientAvatar != null) {
                            AsyncImage(
                                model = item.clientAvatar,
                                contentDescription = item.clientName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        } else {
                            Text(
                                text = item.clientName.firstOrNull()?.toString() ?: "",
                                style = LessTheme.typography.caption12Bold,
                                color = LessTheme.colors.textIconsGrey
                            )
                        }
                    }
                    Text(
                        text = item.clientName,
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsBlack,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Reserve number badge
                Box(
                    modifier = Modifier
                        .background(
                            color = LessTheme.colors.backgroundSecond,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = LessTheme.spacing.xxSmall)
                ) {
                    Text(
                        text = "#${item.reserveNumber}",
                        style = LessTheme.typography.body16Medium,
                        color = LessTheme.colors.textIconsBlack
                    )
                }
            }

            HorizontalDivider(color = LessTheme.colors.backgroundSecond, thickness = 1.dp)

            // Pickup time + quantity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.pickupTimeFormatted,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey
                )
                Text(
                    text = "x${item.quantity}",
                    style = LessTheme.typography.body14Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }

        // Handed Over button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
                .padding(bottom = LessTheme.spacing.small)
        ) {
            DsButton(
                text = stringResource(Res.string.merch_orders_handed_over),
                onClick = onHandedOverClick,
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Medium
            )
        }
    }
}

/**
 * Card for created boxes (awaiting purchase tab)
 */
@Composable
fun CreatedBoxCard(
    item: CreatedBoxItem,
    onCardClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
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
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Available items badge
            Box(
                modifier = Modifier
                    .padding(start = 14.dp, top = 14.dp)
                    .align(Alignment.TopStart)
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(1000.dp)
                    )
                    .padding(horizontal = LessTheme.spacing.xSmall, vertical = LessTheme.spacing.xxSmall)
            ) {
                Text(
                    text = stringResource(Res.string.merch_orders_items_left, item.availableItems),
                    style = LessTheme.typography.caption12Semibold,
                    color = LessTheme.colors.textIconsNested
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Price row
            PriceRow(originalPrice = item.originalPrice, discountedPrice = item.discountedPrice)

            // Box title
            Text(
                text = item.title,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(color = LessTheme.colors.backgroundSecond, thickness = 1.dp)

            // Pickup time + sold/total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.pickupTimeFormatted,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey
                )
                Text(
                    text = "${item.soldCount}/${item.quantity}",
                    style = LessTheme.typography.body14Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }

            // Cancellation message if present
            if (item.cancellationMessage != null) {
                Text(
                    text = item.cancellationMessage,
                    style = LessTheme.typography.caption12Regular,
                    color = LessTheme.colors.textIconsGrey
                )
            }
        }

        // Cancel button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.small)
                .padding(bottom = LessTheme.spacing.small)
        ) {
            if (item.isCancelEnabled) {
                DsButton(
                    text = item.cancelButtonText ?: "",
                    onClick = onCancelClick,
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.Secondary,
                    size = ButtonSize.Medium,
                    textColor = LessTheme.colors.textIconsError
                )
            } else {
                DsButton(
                    text = stringResource(Res.string.merch_orders_cancellation_ended),
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.Secondary,
                    size = ButtonSize.Medium,
                    enabled = false
                )
            }
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
        // Original price (strikethrough)
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "%.2f".format(originalPrice),
                style = LessTheme.typography.body14Semibold.copy(textDecoration = TextDecoration.LineThrough),
                color = LessTheme.colors.textIconsGrey
            )
            Text(
                text = "₼",
                style = LessTheme.typography.caption12Semibold.copy(textDecoration = TextDecoration.LineThrough),
                color = LessTheme.colors.textIconsGrey
            )
        }

        // Discounted price
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
