package az.less.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme

/**
 * A bottom sheet component for displaying scrollable text content with a title and close button.
 * 
 * @param title The title text displayed at the top of the bottom sheet
 * @param content The text content to display (scrollable)
 * @param onDismiss Callback when the bottom sheet is dismissed
 * @param closeButtonText Text for the close button (default: "Close")
 * @param modifier Optional modifier for the bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsTextBottomSheet(
    title: String,
    content: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    closeButtonText: String = "Close"
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

            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = content,
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsGrey,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            // Close button
            DsButton(
                text = closeButtonText,
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Secondary
            )
        }
    }
}

