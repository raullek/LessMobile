package az.less.mobile.presentation.client.main.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.presentation.client.main.explore.models.FilterCategory
import az.less.mobile.presentation.client.main.explore.models.FilterData
import az.less.mobile.presentation.client.main.explore.models.FilterIconType
import az.less.mobile.presentation.client.main.explore.models.FilterOption
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.filter_title
import lessmobile.composeapp.generated.resources.action_apply_filters
import org.jetbrains.compose.resources.stringResource

/**
 * Filter Bottom Sheet component
 * Displays filter categories with options as chips
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2549-54527&m=dev
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    isVisible: Boolean,
    sheetState: SheetState,
    filterData: az.less.mobile.presentation.client.main.explore.models.FilterData,
    onFilterOptionClicked: (String, String) -> Unit, // categoryId, optionId
    onDismiss: () -> Unit,
    onApplyFilters: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = LessTheme.colors.backgroundPrimary,
            contentColor = LessTheme.colors.textIconsBlack,
            shape = RoundedCornerShape(LessTheme.radius.medium, LessTheme.radius.medium),
            dragHandle = null, // Remove default drag handle
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 34.dp) // Home indicator space
            ) {
                // Custom drag handle inside shaped container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(LessTheme.colors.borderPrimary)
                    )
                }
                
                // Filter content with button at bottom
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Filter content
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(
                            horizontal = LessTheme.spacing.medium,
                            vertical = LessTheme.spacing.medium
                        ),
                        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.large)
                    ) {
                        // Title item
                        item {
                            Text(
                                text = stringResource(Res.string.filter_title),
                                style = LessTheme.typography.title24Bold,
                                color = LessTheme.colors.textIconsBlack
                            )
                        }
                        
                        // Filter categories with options
                        items(
                            items = filterData.categories,
                            key = { it.id }
                        ) { category ->
                            _root_ide_package_.az.less.mobile.presentation.client.main.explore.components.FilterCategorySection(
                                category = category,
                                onOptionClicked = { optionId ->
                                    onFilterOptionClicked(category.id, optionId)
                                }
                            )
                        }
                    }
                    
                    // Apply Filters button at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = LessTheme.spacing.medium,
                                vertical = LessTheme.spacing.medium
                            )
                    ) {
                        DsButton(
                            text = stringResource(Res.string.action_apply_filters),
                            onClick = onApplyFilters,
                            variant = ButtonVariant.Primary,
                            size = ButtonSize.Large,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

/**
 * Filter category section with title and option chips
 */
@Composable
private fun FilterCategorySection(
    category: az.less.mobile.presentation.client.main.explore.models.FilterCategory,
    onOptionClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
    ) {
        // Category title
        Text(
            text = category.title,
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsSecondary
        )
        
        // Filter option chips in a row (wrap if needed)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small),
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
        ) {
            category.options.forEach { option ->
                _root_ide_package_.az.less.mobile.presentation.client.main.explore.components.FilterChip(
                    text = option.text,
                    backgroundColor = LessTheme.colors.elementsSecondaryElement,
                    iconType = _root_ide_package_.az.less.mobile.presentation.client.main.explore.models.FilterIconType.NONE,
                    isSelected = option.isSelected,
                    onClick = { onOptionClicked(option.id) },
                )
            }
        }
    }
}

