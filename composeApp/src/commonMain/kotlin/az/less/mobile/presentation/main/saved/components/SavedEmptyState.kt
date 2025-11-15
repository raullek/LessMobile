package az.less.mobile.presentation.main.saved.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_saved_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Empty state component for Saved/Favorites screen
 * Shows when user has no favorite items
 */
@Composable
fun SavedEmptyState(
    onExploreNewVenuesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Heart Icon
        Icon(
            painter = painterResource(Res.drawable.ic_saved_24dp),
            contentDescription = "No favorites",
            tint = LessTheme.colors.textIconsBrand,
            modifier = Modifier.size(LessTheme.size.xxLarge)
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
        
        // Title
        Text(
            text = "You don't have favorites",
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))
        
        // Description
        Text(
            text = "Your saved items from all categories will appear here. You can delete or use them at any time",
            style = LessTheme.typography.body16Regular,
            color = LessTheme.colors.textIconsThird,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
        
        // Button
        DsButton(
            text = "Explore new venues",
            onClick = onExploreNewVenuesClick,
            size = ButtonSize.Large,
            variant = ButtonVariant.Primary
        )
    }
}

