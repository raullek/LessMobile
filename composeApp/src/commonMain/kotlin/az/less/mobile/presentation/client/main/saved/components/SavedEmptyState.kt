package az.less.mobile.presentation.client.main.saved.components

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
import lessmobile.composeapp.generated.resources.saved_empty_title
import lessmobile.composeapp.generated.resources.saved_empty_description
import lessmobile.composeapp.generated.resources.saved_explore_venues
import lessmobile.composeapp.generated.resources.cd_favorite
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
            contentDescription = stringResource(Res.string.cd_favorite),
            tint = LessTheme.colors.textIconsBrand,
            modifier = Modifier.size(LessTheme.size.xxLarge)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Title
        Text(
            text = stringResource(Res.string.saved_empty_title),
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        // Description
        Text(
            text = stringResource(Res.string.saved_empty_description),
            style = LessTheme.typography.body16Regular,
            color = LessTheme.colors.textIconsThird,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Button
        DsButton(
            text = stringResource(Res.string.saved_explore_venues),
            onClick = onExploreNewVenuesClick,
            size = ButtonSize.Large,
            variant = ButtonVariant.Primary
        )
    }
}

