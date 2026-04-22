package az.less.mobile.presentation.partner.places.edit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.TextFieldColors
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.edit_profile_add_for_lots
import lessmobile.composeapp.generated.resources.edit_profile_box_description_placeholder
import lessmobile.composeapp.generated.resources.edit_profile_default_box_description
import lessmobile.composeapp.generated.resources.ic_edit_24dp
import lessmobile.composeapp.generated.resources.image_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Default box description section
 */
@Composable
fun DefaultBoxDescriptionSection(
    defaultBoxDescription: String,
    onDefaultBoxDescriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium)
    ) {
        Text(
            text = stringResource(Res.string.edit_profile_default_box_description),
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsSecondary
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.small))

        DsTextField(
            value = defaultBoxDescription,
            onValueChange = onDefaultBoxDescriptionChanged,
            placeholder = stringResource(Res.string.edit_profile_box_description_placeholder),
            singleLine = false,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldColors(backgroundColorDefault = LessTheme.colors.elementsSecondaryElement)
        )
    }
}

/**
 * Add for lots image section matching Figma design
 */
@Composable
fun AddForLotsSection(
    lotsImageUrl: String?,
    lotsImageBytes: ByteArray?,
    onLotsEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasImage = lotsImageUrl != null || lotsImageBytes != null

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .clickable { onLotsEditClick() },
            contentAlignment = Alignment.Center
        ) {
            if (hasImage) {
                // Show selected image
                if (lotsImageBytes != null) {
                    AsyncImage(
                        model = lotsImageBytes,
                        contentDescription = "Lots image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (lotsImageUrl != null) {
                    AsyncImage(
                        model = lotsImageUrl,
                        contentDescription = "Lots image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Image(
                    painter = painterResource(Res.drawable.image_placeholder),
                    contentDescription = "empty image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Show placeholder text when no image
                Text(
                    text = stringResource(Res.string.edit_profile_add_for_lots),
                    style = LessTheme.typography.title28Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }

            // Edit icon overlay in circular button
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = LessTheme.spacing.medium)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0x80515E5E))
                    .clickable { onLotsEditClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit_24dp),
                    contentDescription = "Edit lots image",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * Combined additional sections: Default box description and Add for lots
 */
@Composable
fun EditBranchAdditionalSections(
    defaultBoxDescription: String,
    lotsImageUrl: String?,
    lotsImageBytes: ByteArray?,
    onDefaultBoxDescriptionChanged: (String) -> Unit,
    onLotsEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
    ) {
        // Default Box Description Section
        DefaultBoxDescriptionSection(
            defaultBoxDescription = defaultBoxDescription,
            onDefaultBoxDescriptionChanged = onDefaultBoxDescriptionChanged
        )

        // Add for Lots Section
        AddForLotsSection(
            lotsImageUrl = lotsImageUrl,
            lotsImageBytes = lotsImageBytes,
            onLotsEditClick = onLotsEditClick
        )
    }
}

