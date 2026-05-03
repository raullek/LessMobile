package az.less.mobile.presentation.client.reserve

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsTextField
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_star_24dp
import lessmobile.composeapp.generated.resources.leave_review_input_placeholder
import lessmobile.composeapp.generated.resources.leave_review_submit
import lessmobile.composeapp.generated.resources.leave_review_subtitle
import lessmobile.composeapp.generated.resources.leave_review_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Bottom sheet shown after a previous order is selected and the user taps
 * "Leave review". Owns its own local state (rating + comment) and only emits
 * the final payload via [onSubmit] — the host doesn't have to lift any of
 * that state.
 *
 * Figma: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/?node-id=3057-11279
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveReviewBottomSheet(
    isVisible: Boolean,
    sheetState: SheetState,
    venueName: String,
    onSubmit: (rating: Int, comment: String) -> Unit,
    onDismiss: () -> Unit,
    isSubmitting: Boolean = false
) {
    if (!isVisible) return

    // Local state — survives configuration changes and back-stack restoration
    // but resets when the sheet is dismissed (key on venueName so a different
    // order's sheet starts fresh).
    var rating by rememberSaveable(venueName) { mutableStateOf(0) }
    var comment by rememberSaveable(venueName) { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = LessTheme.colors.backgroundSecond,
        contentColor = LessTheme.colors.textIconsBlack,
        shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
        dragHandle = null,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
        ) {
            // Drag handle (32×3 brand-token line) — Figma node 3057:11281.
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Header — title + venue-name subtitle. Figma px=24, gap=8,
                // text aligned center inside the bottom sheet's content area.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.leave_review_title),
                        style = LessTheme.typography.title20Bold,
                        color = LessTheme.colors.textIconsBlack,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(Res.string.leave_review_subtitle, venueName),
                        style = LessTheme.typography.body16Medium,
                        color = LessTheme.colors.textIconsGrey,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Rating row + review input — Figma vertical gap 16, both
                // centered horizontally within the sheet.
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Five stars: 36×36, gap 16, vertical padding 20 (Figma).
                    Row(
                        modifier = Modifier.padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(5) { index ->
                            val position = index + 1
                            val isFilled = position <= rating
                            // Use a separate interaction source per star with
                            // no ripple — design shows only colour change.
                            Icon(
                                painter = painterResource(Res.drawable.ic_star_24dp),
                                contentDescription = null,
                                tint = if (isFilled) {
                                    LessTheme.colors.textIconsWarning
                                } else {
                                    LessTheme.colors.textIconsThird
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { rating = position }
                            )
                        }
                    }

                    // Review text input — gray rounded card per Figma. Uses
                    // DsTextField so the design-system focus / disabled states
                    // come along for free.
                    DsTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        placeholder = stringResource(Res.string.leave_review_input_placeholder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

                // Submit button — disabled until the user picks at least one
                // star, and shows the loading spinner while the request is in
                // flight (host owns the request, passes [isSubmitting] back).
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    DsButton(
                        text = stringResource(Res.string.leave_review_submit),
                        onClick = { onSubmit(rating, comment.trim()) },
                        modifier = Modifier.fillMaxWidth(),
                        variant = ButtonVariant.Primary,
                        size = ButtonSize.Large,
                        enabled = rating > 0 && !isSubmitting,
                        isLoading = isSubmitting
                    )
                }

                // Home indicator space (34dp).
                Spacer(modifier = Modifier.height(34.dp))
            }
        }
    }
}
