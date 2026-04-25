package az.less.mobile.presentation.partner.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_terms_file_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Download icon button component
 * Based on Figma design - 48x48dp white rounded button with download icon
 */
@Composable
fun DownloadIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(LessTheme.size.xxLarge) // 48dp
            .background(
                color = LessTheme.colors.backgroundPrimary,
                shape = RoundedCornerShape(LessTheme.radius.medium) // 16dp
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Using terms_file icon as placeholder for download icon
        // TODO: Replace with actual download icon when available
        Icon(
            painter = painterResource(Res.drawable.ic_terms_file_24dp),
            contentDescription = "Download",
            modifier = Modifier.size(LessTheme.spacing.medium), // 24dp
            tint = LessTheme.colors.textIconsBlack
        )
    }
}

