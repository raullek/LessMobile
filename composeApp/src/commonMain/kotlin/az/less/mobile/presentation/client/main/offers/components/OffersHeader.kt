package az.less.mobile.presentation.client.main.offers.components

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
import az.less.designsystem.base.LessTheme
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_notification_24dp
import lessmobile.composeapp.generated.resources.person_image_placeholder
import org.jetbrains.compose.resources.painterResource

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
                .size(LessTheme.size.xLarge)
                .clip(CircleShape)
                .background(LessTheme.colors.elementsSecondaryElement),
            contentAlignment = Alignment.Center
        ) {
                AsyncImage(
                    model = userAvatarUrl,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(LessTheme.size.xLarge)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(Res.drawable.person_image_placeholder),
                    placeholder = painterResource(Res.drawable.person_image_placeholder)
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

        // Notification Button
        Box(
            modifier = Modifier
                .size(LessTheme.size.xLarge + LessTheme.spacing.xxSmall)
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.textIconsNested)
                .clickable { onMessageClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_notification_24dp),
                contentDescription = "Messages",
                modifier = Modifier.size(LessTheme.size.small + LessTheme.spacing.xxxSmall),
                tint = LessTheme.colors.textIconsBlack
            )
        }
    }
}
