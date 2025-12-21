package az.less.mobile.presentation.merchant.places.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_edit_24dp
import lessmobile.composeapp.generated.resources.ic_map_24dp
import lessmobile.composeapp.generated.resources.ic_phone_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Contact section with phone and location
 * Based on MerchantProfileContactSection but with Edit buttons instead of Directions
 */
@Composable
fun EditMerchantProfileContactSection(
    phoneNumber: String,
    location: String,
    onPhoneEditClick: () -> Unit,
    onLocationEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Edit text with inline edit icon (shared for both fields)
    val editText = "Edit"
    val editAnnotatedText = remember {
        buildAnnotatedString {
            append(editText)
            // Add space before icon
            append("  ")
            // Add inline content placeholder for edit icon
            appendInlineContent("edit_icon", "\uFFFC")
        }
    }

    val editIconPainter = painterResource(Res.drawable.ic_edit_24dp)
    val editInlineContent: Map<String, InlineTextContent> = remember(editIconPainter) {
        mapOf(
            "edit_icon" to InlineTextContent(
                placeholder = Placeholder(
                    width = 1.em,
                    height = 1.em,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                Icon(
                    painter = editIconPainter,
                    contentDescription = "Edit",
                    tint = LessTheme.colors.textIconsBrand,
                    modifier = Modifier.size(16.dp)
                )
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium)
    ) {
        // Phone Number Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPhoneEditClick() }
                .padding(vertical = LessTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Phone Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.backgroundSecond),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_phone_24dp),
                    contentDescription = "Phone",
                    tint = LessTheme.colors.textIconsBlack,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(LessTheme.spacing.small))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Phone number",
                    style = LessTheme.typography.body16Regular,
                    color = LessTheme.colors.textIconsBlack
                )
                Text(
                    text = if (phoneNumber.isEmpty()) "Optional" else phoneNumber,
                    style = LessTheme.typography.caption12Regular,
                    color = LessTheme.colors.textIconsGrey
                )
            }

            // Edit text with inline edit icon (vertically centered by Row)
            Text(
                text = editAnnotatedText,
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsBrand,
                inlineContent = editInlineContent
            )
        }

        // Location Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLocationEditClick() }
                .padding(vertical = LessTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.backgroundSecond),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_map_24dp),
                    contentDescription = "Location",
                    tint = LessTheme.colors.textIconsBlack,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(LessTheme.spacing.small))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "View location",
                    style = LessTheme.typography.body16Regular,
                    color = LessTheme.colors.textIconsBlack
                )
                Text(
                    text = location.ifEmpty { "Location of your perfect place" },
                    style = LessTheme.typography.caption12Regular,
                    color = LessTheme.colors.textIconsGrey,
                    maxLines = 1
                )
            }

            // Edit text with inline edit icon (vertically centered by Row)
            Text(
                text = editAnnotatedText,
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsBrand,
                inlineContent = editInlineContent
            )
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
    }
}

