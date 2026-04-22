package az.less.mobile.presentation.partner.places.edit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.edit_profile_description_placeholder
import lessmobile.composeapp.generated.resources.edit_profile_name_placeholder
import lessmobile.composeapp.generated.resources.ic_edit_24dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Merchant profile info section with name, description, and edit icon
 * Based on MerchantProfileInfoSection but with edit icon and editable description
 */
@Composable
fun EditMerchantProfileInfoSection(
    venueName: String,
    description: String,
    onNameEditClick: () -> Unit,
    onDescriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Venue Name with Edit Icon
        Text(
            text = venueName.ifEmpty { stringResource(Res.string.edit_profile_name_placeholder) },
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        // Description Text with Edit Icon inline at the end
        val text = description.ifEmpty { stringResource(Res.string.edit_profile_description_placeholder) }
        val textColor = if (description.isEmpty()) LessTheme.colors.textIconsGrey else LessTheme.colors.textIconsBlack
        
        val annotatedText = remember(text) {
            buildAnnotatedString {
                withStyle(
                    style = androidx.compose.ui.text.SpanStyle(
                        color = textColor
                    )
                ) {
                    append(text)
                }
                // Add space before icon
                append(" ")
                // Add inline content placeholder for edit icon
                appendInlineContent("edit_icon", "\uFFFC")
            }
        }

        val inlineContent = remember {
            mapOf(
                "edit_icon" to InlineTextContent(
                    placeholder = Placeholder(
                        width = 0.8.em,
                        height = 0.8.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit_24dp),
                        contentDescription = "Edit description",
                        tint = LessTheme.colors.textIconsGrey,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { onDescriptionChanged(description) }
                    )
                }
            )
        }

        Text(
            text = annotatedText,
            style = LessTheme.typography.body14Regular,
            inlineContent = inlineContent,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDescriptionChanged(description) },
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
    }
}

