package az.less.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.design_system.generated.resources.Res
import lessmobile.design_system.generated.resources.ic_back_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Design system Top Bar component.
 * Reusable toolbar for all screens:
 * - Back button (optional)
 * - Title centered
 * - Actions on the right (optional)
 *
 * Matches UI style of DsTextField:
 * - Uses LessTheme.spacing for consistent padding
 * - Uses LessTheme.typography
 * - Uses LessTheme.colors for icon/text
 */
@Composable
fun DsToolBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    backIcon: Painter = painterResource(Res.drawable.ic_back_24dp),
    actions: @Composable (() -> Unit)? = null,
    backgroundColor: Color = LessTheme.colors.backgroundPrimary
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(56.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (onBackClick != null) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                onClick = onBackClick
            ) {
                Icon(
                    painter = backIcon,
                    contentDescription = "Back",
                    tint = LessTheme.colors.textIconsBlack
                )
            }
        }

        Text(
            text = title,
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsBlack
        )

        if (actions != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = LessTheme.spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                actions()
            }
        }
    }
}
