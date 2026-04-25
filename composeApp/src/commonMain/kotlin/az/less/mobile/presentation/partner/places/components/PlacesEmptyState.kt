package az.less.mobile.presentation.partner.places.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ill_branch_venue
import lessmobile.composeapp.generated.resources.image_placeholder
import lessmobile.composeapp.generated.resources.places_add_branch
import lessmobile.composeapp.generated.resources.places_cd_no_places
import lessmobile.composeapp.generated.resources.places_empty_description
import lessmobile.composeapp.generated.resources.places_empty_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Empty state component for Merchant Places screen
 * Shows when merchant has no branches registered
 */
@Composable
fun PlacesEmptyState(
    onAddBranchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Placeholder Image
        Image(
            painter = painterResource(Res.drawable.ill_branch_venue),
            contentDescription = stringResource(Res.string.places_cd_no_places),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(LessTheme.size.xxLarge)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Title
        Text(
            text = stringResource(Res.string.places_empty_title),
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        // Description
        Text(
            text = stringResource(Res.string.places_empty_description),
            style = LessTheme.typography.body16Regular,
            color = LessTheme.colors.textIconsThird,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Button
        DsButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium),
            text = stringResource(Res.string.places_add_branch),
            onClick = onAddBranchClick,
            size = ButtonSize.Large,
            variant = ButtonVariant.Primary
        )
    }
}
