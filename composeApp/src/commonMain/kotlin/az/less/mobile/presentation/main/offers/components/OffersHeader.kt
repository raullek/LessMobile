package az.less.mobile.presentation.main.offers.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.compose_multiplatform
import lessmobile.composeapp.generated.resources.ic_notification_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Header component for Offers screen with user avatar, greeting, and notification buttons
 * Based on Figma design: "Hi, Katheryn!" with two white circular buttons
 */
@Composable
fun OffersHeader(
    userName: String,
    onNotificationClick: () -> Unit,
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier,
    userAvatarUrl: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Avatar
        Box(
            modifier = Modifier
                .size(LessTheme.size.xxLarge)
                .clip(CircleShape)
                .background(LessTheme.colors.elementsSecondaryElement),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(LessTheme.size.xxLarge)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.width(LessTheme.spacing.xSmall))
        
        // User Greeting
        Text(
            text = "Hi, $userName!",
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsBlack,
            modifier = Modifier.weight(1f)
        )

        
        // Notification Button 2 (Message/Bell icon)
        Box(
            modifier = Modifier
                .size(LessTheme.size.xLarge + LessTheme.spacing.xxSmall) // 40 + 4 = 44dp
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.textIconsNested)
                .clickable { onMessageClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_notification_24dp),
                contentDescription = "Messages",
                modifier = Modifier.size(LessTheme.size.small + LessTheme.spacing.xxxSmall), // 20 + 2 = 22dp
                tint = LessTheme.colors.textIconsBlack
            )
        }
    }
}
