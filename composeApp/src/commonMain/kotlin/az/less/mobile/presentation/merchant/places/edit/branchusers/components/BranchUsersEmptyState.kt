package az.less.mobile.presentation.merchant.places.edit.branchusers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.branch_users_add_user
import lessmobile.composeapp.generated.resources.branch_users_cd_no_users
import lessmobile.composeapp.generated.resources.branch_users_empty_description
import lessmobile.composeapp.generated.resources.branch_users_empty_title
import lessmobile.composeapp.generated.resources.ic_account_24dp
import lessmobile.composeapp.generated.resources.ic_plus_24dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Empty state component for Branch Users screen
 * Shows when branch has no users registered
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2394-31370
 */
@Composable
fun BranchUsersEmptyState(
    onAddUserClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // User icon with plus badge
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center
        ) {
            // Main user icon
            Icon(
                painter = painterResource(Res.drawable.ic_account_24dp),
                contentDescription = stringResource(Res.string.branch_users_cd_no_users),
                tint = LessTheme.colors.textIconsBrand,
                modifier = Modifier.size(48.dp)
            )
            // Plus badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(LessTheme.colors.textIconsBrand),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_plus_24dp),
                    contentDescription = null,
                    tint = LessTheme.colors.textIconsNested,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Title
        Text(
            text = stringResource(Res.string.branch_users_empty_title),
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        // Description
        Text(
            text = stringResource(Res.string.branch_users_empty_description),
            style = LessTheme.typography.body16Regular,
            color = LessTheme.colors.textIconsGrey,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

        // Button
        DsButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.branch_users_add_user),
            onClick = onAddUserClick,
            size = ButtonSize.Large,
            variant = ButtonVariant.Primary
        )
    }
}

