package az.less.mobile.presentation.partner.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.less.designsystem.DsIcons
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.partner.history.model.VenueOption
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.history_all_venues
import lessmobile.composeapp.generated.resources.history_select_venue
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueSelectionBottomSheet(
    venues: List<VenueOption>,
    selectedVenue: VenueOption?,
    onSelect: (VenueOption) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            color = LessTheme.colors.textIconsThird,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        },
        containerColor = LessTheme.colors.backgroundPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium)
                .padding(bottom = LessTheme.spacing.medium)
        ) {
            Text(
                text = stringResource(Res.string.history_select_venue),
                style = LessTheme.typography.title24Semibold,
                color = LessTheme.colors.textIconsBlack
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.heightIn(max = 480.dp)
            ) {
                item(key = "__all__") {
                    VenueRow(
                        title = stringResource(Res.string.history_all_venues),
                        subtitle = null,
                        selected = selectedVenue == null,
                        onClick = onClear
                    )
                }
                items(
                    items = venues,
                    key = { it.id }
                ) { venue ->
                    VenueRow(
                        title = venue.name,
                        subtitle = venue.address.takeIf { it.isNotBlank() },
                        selected = selectedVenue?.id == venue.id,
                        onClick = { onSelect(venue) }
                    )
                }
            }
        }
    }
}

@Composable
private fun VenueRow(
    title: String,
    subtitle: String?,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
        ) {
            Text(
                text = title,
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsGrey,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(LessTheme.spacing.medium))

        Icon(
            painter = if (selected) DsIcons.RadioSelected else DsIcons.RadioUnselected,
            contentDescription = null,
            tint = if (selected) LessTheme.colors.elementsPrimaryBrand
            else LessTheme.colors.textIconsThird
        )
    }
}
