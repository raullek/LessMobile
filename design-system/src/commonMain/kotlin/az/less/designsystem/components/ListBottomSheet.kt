package az.less.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme

/**
 * Data class representing an item in the list bottom sheet
 */
data class ListBottomSheetItem(
    val id: String,
    val title: String,
    val icon: Painter? = null,
    val iconUrl: String? = null
)

/**
 * A reusable bottom sheet component that displays a list of items with icons and chevrons.
 * Each item can be clicked to perform an action.
 * 
 * @param title The title text displayed at the top of the bottom sheet
 * @param items List of items to display
 * @param onItemClick Callback when an item is clicked, receives the item's id
 * @param onDismiss Callback when the bottom sheet is dismissed
 * @param modifier Optional modifier for the bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsListBottomSheet(
    title: String,
    items: List<ListBottomSheetItem>,
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    chevronIcon: Painter? = null
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
        containerColor = LessTheme.colors.backgroundPrimary,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium)
                .padding(bottom = LessTheme.spacing.medium)
        ) {
            // Title
            Text(
                text = title,
                style = LessTheme.typography.title24Semibold,
                color = LessTheme.colors.textIconsBlack
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            // List of items
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall)
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { item ->
                    ListBottomSheetRow(
                        item = item,
                        onClick = { onItemClick(item.id) },
                        chevronIcon = chevronIcon
                    )
                }
            }
        }
    }
}

@Composable
private fun ListBottomSheetRow(
    item: ListBottomSheetItem,
    onClick: () -> Unit,
    chevronIcon: Painter?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = LessTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        // Icon
        if (item.icon != null) {
            Box(
                modifier = Modifier
                    .size(LessTheme.size.medium)
                    .clip(RoundedCornerShape(LessTheme.radius.small)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = item.icon,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else if (item.iconUrl != null) {
            // TODO: Use AsyncImage for URL-based icons if needed
            // For now, we'll just show a placeholder
            Box(
                modifier = Modifier
                    .size(LessTheme.size.medium)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.elementsSecondaryElement)
            )
        }

        // Title
        Text(
            text = item.title,
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsBlack,
            modifier = Modifier.weight(1f)
        )

        // Chevron
        if (chevronIcon != null) {
            Icon(
                painter = chevronIcon,
                contentDescription = null,
                tint = LessTheme.colors.textIconsThird,
                modifier = Modifier.size(LessTheme.size.medium)
            )
        }
    }
}

