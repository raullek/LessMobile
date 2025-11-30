package az.less.designsystem.components

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
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.DsIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DsSelectionBottomSheet(
    title: String,
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit,
    itemTitle: (T) -> String = { it.toString() }
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
                text = title,
                style = LessTheme.typography.title24Semibold,
                color = LessTheme.colors.textIconsBlack
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn {
                items(items) { item ->
                    SelectionRow(
                        text = itemTitle(item),
                        selected = item == selectedItem,
                        onClick = { onSelect(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = text,
            style = LessTheme.typography.body14Regular,
            color = LessTheme.colors.textIconsBlack
        )

        Icon(
            painter = if (selected) DsIcons.RadioSelected
            else DsIcons.RadioUnselected,
            contentDescription = null,
            tint = if (selected) LessTheme.colors.elementsPrimaryBrand
            else LessTheme.colors.textIconsThird
        )
    }
}
