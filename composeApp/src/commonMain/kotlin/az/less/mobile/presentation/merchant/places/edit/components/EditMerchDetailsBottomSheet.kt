package az.less.mobile.presentation.merchant.places.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsTextField

/**
 * Bottom sheet for editing merchant details (title and description)
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2356-9476
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMerchDetailsBottomSheet(
    isVisible: Boolean,
    sheetState: SheetState,
    initialTitle: String,
    initialDescription: String,
    onSave: (title: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    // Local state for editing
    var title by remember(initialTitle) { mutableStateOf(initialTitle) }
    var description by remember(initialDescription) { mutableStateOf(initialDescription) }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = LessTheme.colors.backgroundPrimary,
            contentColor = LessTheme.colors.textIconsBlack,
            shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Custom drag handle
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

                // Content
                EditMerchDetailsBottomSheetContent(
                    title = title,
                    description = description,
                    onTitleChange = { title = it },
                    onDescriptionChange = { description = it },
                    onSaveClick = { onSave(title, description) }
                )
            }
        }
    }
}

/**
 * Stateless content for EditMerchDetailsBottomSheet
 */
@Composable
private fun EditMerchDetailsBottomSheetContent(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium)
            .padding(bottom = 34.dp) // Home indicator space
    ) {
        // Title
        Text(
            text = "Edit merch details",
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Title TextField
        DsTextField(
            value = title,
            onValueChange = onTitleChange,
            label = "Title",
            placeholder = "Enter venue name",
            modifier = Modifier.fillMaxWidth(),
            onEndIconClick = { onTitleChange("") }
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Description TextField
        DsTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = "Description",
            placeholder = "Enter description",
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 3,
            onEndIconClick = { onDescriptionChange("") }
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Save button
        DsButton(
            text = "Save",
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            variant = ButtonVariant.Primary,
            size = ButtonSize.Large
        )
    }
}

