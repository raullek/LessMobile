package az.less.mobile.presentation.merchant.places.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton

/**
 * Branch Verification Screen - shown after venue/branch was added
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2390-30553&m=dev
 */
@Composable
fun BranchVerificationScreen(
    navController: NavController,
    onAddUsersClicked: () -> Unit,
    onHomeClicked: () -> Unit
) {
    BranchVerificationScreenContent(
        onAddUsersClicked = onAddUsersClicked,
        onHomeClicked = onHomeClicked
    )
}

@Composable
private fun BranchVerificationScreenContent(
    onAddUsersClicked: () -> Unit,
    onHomeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top spacer to push content to center
        Spacer(modifier = Modifier.weight(1f))

        // Center content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Success checkmark icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(LessTheme.colors.textIconsBrand),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    style = LessTheme.typography.display36Semibold,
                    color = Color.White,
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            // Title
            Text(
                text = "Venue was added",
                style = LessTheme.typography.title20Semibold,
                color = LessTheme.colors.textIconsBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            // Description
            Text(
                text = "If you need some custom edits",
                style = LessTheme.typography.body14Medium.copy(
                    lineHeight = 22.sp
                ),
                color = LessTheme.colors.textIconsGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Bottom spacer
        Spacer(modifier = Modifier.weight(1f))

        // Bottom buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium)
                .padding(bottom = LessTheme.spacing.medium)
        ) {
            // Add users button (Primary)
            DsButton(
                text = "Add users",
                onClick = onAddUsersClicked,
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            // Home button (Tertiary - text only)
            DsButton(
                text = "Home",
                onClick = onHomeClicked,
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Tertiary,
                size = ButtonSize.Large
            )

            Spacer(modifier = Modifier.height(34.dp)) // Home indicator space
        }
    }
}

