package az.less.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme

/**
 * A confirmation bottom sheet component with image, title, description, and two action buttons.
 * 
 * @param image Painter for the image/icon to display at the top
 * @param title The title text (e.g., "Delete Mastercard •••• 2412")
 * @param description The description text explaining the action
 * @param primaryButtonText Text for the primary action button (e.g., "Delete")
 * @param secondaryButtonText Text for the secondary action button (e.g., "Cancel")
 * @param onPrimaryClick Callback when primary button is clicked
 * @param onSecondaryClick Callback when secondary button is clicked
 * @param onDismiss Callback when the bottom sheet is dismissed
 * @param primaryButtonEnabled Whether the primary button is enabled
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsConfirmationBottomSheet(
    image: Painter,
    title: String,
    description: String,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    primaryButtonEnabled: Boolean = true,
    primaryButtonLoading: Boolean = false
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
            // Image container with white rounded background (centered)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LessTheme.spacing.small),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(LessTheme.size.medium)
                        .clip(RoundedCornerShape(LessTheme.radius.small))
                        .background(LessTheme.colors.elementsPrimaryElement)
                        .padding(LessTheme.spacing.xxxSmall),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = image,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            // Title
            Text(
                text = title,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            // Description
            Text(
                text = description,
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsGrey,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            // Primary button
            DsButton(
                text = primaryButtonText,
                onClick = onPrimaryClick,
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                enabled = primaryButtonEnabled,
                isLoading = primaryButtonLoading
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            // Secondary button (text-only)
            DsButton(
                text = secondaryButtonText,
                onClick = onSecondaryClick,
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Tertiary
            )
        }
    }
}

