package az.less.mobile.presentation.merchant.places.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.merchant.places.model.BranchItem
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_star_16dp
import lessmobile.composeapp.generated.resources.ill_box_placeholder
import lessmobile.composeapp.generated.resources.ill_venue_placeholder
import lessmobile.composeapp.generated.resources.places_default_branch_tag
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Card component for displaying a merchant branch
 * Matches Figma design with large image, badges, and details
 */
@Composable
fun BranchCard(
    branch: BranchItem,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDefault: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.backgroundPrimary)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Large Image Section with Overlay Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp) // Image height from design
            ) {
                // Branch Cover Image
                AsyncImage(
                    model = branch.imageUrl,
                    contentDescription = branch.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                topStart = LessTheme.radius.medium,
                                topEnd = LessTheme.radius.medium
                            )
                        ),
                    placeholder = painterResource(Res.drawable.ill_box_placeholder),
                    error = painterResource(Res.drawable.ill_box_placeholder)
                )

                // Top-left Badges: items-on-sale / no-active-offer + optional default-branch tag
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(LessTheme.spacing.small),
                    verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(LessTheme.radius.xSmall))
                            .background(
                                if (branch.hasActiveDiscount && branch.itemsOnSale > 0)
                                    LessTheme.colors.textIconsBrand
                                else
                                    LessTheme.colors.textIconsGrey
                            )
                            .padding(
                                horizontal = LessTheme.spacing.xSmall,
                                vertical = LessTheme.spacing.xxxSmall
                            )
                    ) {
                        Text(
                            text = if (branch.hasActiveDiscount && branch.itemsOnSale > 0)
                                "${branch.itemsOnSale} items on sale"
                            else
                                "No active offer",
                            style = LessTheme.typography.caption12Semibold,
                            color = LessTheme.colors.backgroundPrimary
                        )
                    }

                    if (isDefault) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(LessTheme.radius.xSmall))
                                .background(LessTheme.colors.elementsPrimaryBrand)
                                .padding(
                                    horizontal = LessTheme.spacing.xSmall,
                                    vertical = LessTheme.spacing.xxxSmall
                                )
                        ) {
                            Text(
                                text = stringResource(Res.string.places_default_branch_tag),
                                style = LessTheme.typography.caption12Semibold,
                                color = LessTheme.colors.backgroundPrimary
                            )
                        }
                    }
                }
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = LessTheme.spacing.small,
                        start = LessTheme.spacing.medium,
                        end = LessTheme.spacing.medium,
                        bottom = LessTheme.spacing.medium
                    )
            ) {
            // Branch Name
            Text(
                text = branch.name,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))

            // Address
            Text(
                text = branch.address,
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsGrey,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

            // Divider
            Divider(
                color = LessTheme.colors.borderPrimary,
                thickness = 1.dp
            )

            // Rating and Distance Row (only show if rating > 0 or distance is not empty)
            if (branch.rating > 0f || branch.distance.isNotEmpty()) {
                Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
                ) {
                    // Star Icon
                    Icon(
                        painter = painterResource(Res.drawable.ic_star_16dp),
                        contentDescription = "Rating",
                        tint = LessTheme.colors.textIconsWarning,
                        modifier = Modifier.size(16.dp)
                    )

                    // Rating
                    if (branch.rating > 0f) {
                        Text(
                            text = branch.rating.toString(),
                            style = LessTheme.typography.body14Semibold,
                            color = LessTheme.colors.textIconsBlack
                        )
                    }

                    // Dot Separator
                    if (branch.rating > 0f && branch.distance.isNotEmpty()) {
                        Text(
                            text = " \u2022 ",
                            style = LessTheme.typography.body14Regular,
                            color = LessTheme.colors.textIconsBlack
                        )
                    }

                    // Distance
                    if (branch.distance.isNotEmpty()) {
                        Text(
                            text = branch.distance,
                            style = LessTheme.typography.body14Semibold,
                            color = LessTheme.colors.textIconsBlack
                        )
                    }
                }
            }
            }
        }

        // Merchant Logo - positioned independently between image and text sections, aligned to end
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    x = -16.dp,
                    y = (160.dp - 20.dp) // Image height - half overlap
                )
                .size(40.dp)
                .shadow(
                    elevation = LessTheme.elevation.small,
                    shape = RoundedCornerShape(LessTheme.radius.small)
                )
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.backgroundPrimary),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = branch.logoUrl,
                contentDescription = "Branch merchant logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small)),
                placeholder = painterResource(Res.drawable.ill_venue_placeholder),
                error = painterResource(Res.drawable.ill_venue_placeholder)
            )
        }
    }
}
