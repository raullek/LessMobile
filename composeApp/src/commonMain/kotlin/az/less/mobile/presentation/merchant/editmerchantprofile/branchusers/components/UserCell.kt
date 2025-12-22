package az.less.mobile.presentation.merchant.editmerchantprofile.branchusers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * User cell component for Branch Users screen
 * Displays user name with chevron navigation
 */
@Composable
fun UserCell(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .background(LessTheme.colors.elementsSecondaryElement)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(
                horizontal = LessTheme.spacing.medium,
                vertical = LessTheme.spacing.large
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User name
        Text(
            text = name,
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsBlack,
            modifier = Modifier.weight(1f)
        )

        // Chevron icon
        Icon(
            painter = painterResource(Res.drawable.ic_chevron_right_24dp),
            contentDescription = null,
            tint = LessTheme.colors.textIconsThird,
            modifier = Modifier.size(LessTheme.size.medium)
        )
    }
}
