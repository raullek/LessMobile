package az.less.mobile.presentation.merchant.places.edit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsTextField
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_edit_24dp
import lessmobile.composeapp.generated.resources.image_placeholder
import org.jetbrains.compose.resources.painterResource

/**
 * Additional sections: Default box description, Add for lots, and Toggle
 */
@Composable
fun EditBranchAdditionalSections(
    defaultBoxDescription: String,
    lotsImageUrl: String?,
    manageFromEmail: Boolean,
    onDefaultBoxDescriptionChanged: (String) -> Unit,
    onLotsEditClick: () -> Unit,
    onManageFromEmailToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        // Default Box Description Section
        Column {
            Text(
                text = "Default box description",
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsGrey
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

            DsTextField(
                value = defaultBoxDescription,
                onValueChange = onDefaultBoxDescriptionChanged,
                placeholder = "Some tips for user to understant what can be inside",
                singleLine = false,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Add for Lots Section
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.medium))
                    .background(LessTheme.colors.elementsThirdElement)
            ) {
                if (lotsImageUrl != null) {
                    Image(
                        painter = painterResource(Res.drawable.image_placeholder),
                        contentDescription = "Add for lots",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // Show placeholder text when no image
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add for lots",
                            style = LessTheme.typography.body16Semibold,
                            color = LessTheme.colors.textIconsBlack
                        )
                    }
                }

                // Edit icon overlay
                IconButton(
                    onClick = onLotsEditClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(LessTheme.spacing.small)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit_24dp),
                        contentDescription = "Edit lots image",
                        tint = LessTheme.colors.textIconsBlack
                    )
                }
            }
        }

        // Toggle Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Управлять этой точкой буду с этого мейла",
                style = LessTheme.typography.body16Regular,
                color = LessTheme.colors.textIconsBlack,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = manageFromEmail,
                onCheckedChange = onManageFromEmailToggled
            )
        }
    }
}

