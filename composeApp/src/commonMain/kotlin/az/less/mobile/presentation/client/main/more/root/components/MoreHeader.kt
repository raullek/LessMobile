package az.less.mobile.presentation.client.main.more.root.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.DsButton
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.compose_multiplatform
import lessmobile.composeapp.generated.resources.ic_eco_leaf_24dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Header component for More screen that handles two states:
 * 1. Not logged in: Shows app logo, title, description, and login button
 * 2. Logged in: Shows user avatar, name, email, stats, and eco-hero card
 */
@Composable
fun MoreHeader(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    userName: String? = null,
    userEmail: String? = null,
    userAvatarRes: DrawableResource? = null,
    co2Saved: String? = null,
    moneySaved: String? = null,
    ecoHeroTitle: String? = null,
    ecoHeroDescription: String? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        if (isLoggedIn) {
            // Logged in state - show user profile with ecology stats
            _root_ide_package_.az.less.mobile.presentation.client.main.more.root.components.LoggedInHeader(
                userName = userName ?: "User",
                userEmail = userEmail ?: "user@email.com",
                userAvatarRes = userAvatarRes,
                co2Saved = co2Saved ?: "0 kg",
                moneySaved = moneySaved ?: "$0",
                ecoHeroTitle = ecoHeroTitle,
                ecoHeroDescription = ecoHeroDescription
            )
        } else {
            // Not logged in state - show app branding and login prompt
            _root_ide_package_.az.less.mobile.presentation.client.main.more.root.components.NotLoggedInHeader(
                onLoginClick = onLoginClick
            )
        }
    }
}

@Composable
private fun LoggedInHeader(
    userName: String,
    userEmail: String,
    userAvatarRes: DrawableResource?,
    co2Saved: String,
    moneySaved: String,
    ecoHeroTitle: String?,
    ecoHeroDescription: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        // User avatar with colored border
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 2.dp,
                    color = LessTheme.colors.elementsPrimaryBrand,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(LessTheme.colors.elementsSecondaryElement),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(userAvatarRes ?: Res.drawable.compose_multiplatform),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        }
        
        // User name
        Text(
            text = userName,
            style = LessTheme.typography.title20Bold,
            color = LessTheme.colors.textIconsBlack
        )
        
        // User email
        Text(
            text = userEmail,
            style = LessTheme.typography.body14Regular,
            color = LessTheme.colors.textIconsGrey
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))
        
        // Stats Section (CO2 and Money saved)
        _root_ide_package_.az.less.mobile.presentation.client.main.more.root.components.EcologyStatsSection(
            co2Saved = co2Saved,
            moneySaved = moneySaved
        )
        
        // Eco-Hero Card (if available)
        if (ecoHeroTitle != null && ecoHeroDescription != null) {
            _root_ide_package_.az.less.mobile.presentation.client.main.more.root.components.EcoHeroCard(
                title = ecoHeroTitle,
                description = ecoHeroDescription
            )
        }
    }
}

/**
 * Stats section showing CO2 and money saved
 */
@Composable
private fun EcologyStatsSection(
    co2Saved: String,
    moneySaved: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // CO2 Saved
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = co2Saved,
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsBlack
            )
            Spacer(modifier = Modifier.height(LessTheme.spacing.xxxSmall))
            Text(
                text = "CO2, saved",
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsBlack
            )
        }
        
        // Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(34.dp)
                .background(LessTheme.colors.borderPrimary)
        )
        
        // Money Saved
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = moneySaved,
                style = LessTheme.typography.body14Semibold,
                color = LessTheme.colors.textIconsBlack
            )
            Spacer(modifier = Modifier.height(LessTheme.spacing.xxxSmall))
            Text(
                text = "Saved",
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsBlack
            )
        }
    }
}

/**
 * Eco-Hero achievement card
 */
@Composable
private fun EcoHeroCard(
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LessTheme.radius.small))
            .background(LessTheme.colors.elementsSecondaryBrand)
            .padding(LessTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(LessTheme.size.xxLarge)
                .clip(CircleShape)
                .background(LessTheme.colors.elementsPrimaryBrand),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_eco_leaf_24dp),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(LessTheme.size.medium)
            )
        }
        
        // Text content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = LessTheme.typography.body16Regular,
                color = LessTheme.colors.textIconsBlack
            )
            Spacer(modifier = Modifier.height(LessTheme.spacing.xxxSmall))
            Text(
                text = description,
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsBlack
            )
        }
    }
}

@Composable
private fun NotLoggedInHeader(
    onLoginClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
    ) {
        // App logo
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(LessTheme.radius.large))
                .background(LessTheme.colors.elementsPrimaryBrand),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "App Logo",
                modifier = Modifier.size(64.dp)
            )
        }
        
        // Title
        Text(
            text = "Get more features",
            style = LessTheme.typography.title28Bold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )
        
        // Description
        Text(
            text = "Log in to the application to take advantage of special offers from Less",
            style = LessTheme.typography.body16Regular,
            color = LessTheme.colors.textIconsGrey,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.85f)
        )
        
        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))
        
        // Login button
        DsButton(
            text = "Log in or Sign up",
            onClick = onLoginClick,
            size = ButtonSize.Large,
            modifier = Modifier.fillMaxWidth(0.6f)
        )
    }
}

