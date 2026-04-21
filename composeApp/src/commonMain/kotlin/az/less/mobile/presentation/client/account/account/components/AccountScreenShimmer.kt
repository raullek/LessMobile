package az.less.mobile.presentation.client.account.account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun AccountScreenShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .shimmer()
            .padding(horizontal = LessTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))

        // Name field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.small))

        // Phone field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.small))

        // Email field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.small))

        // Gender field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.small))

        // Birthday field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xxLarge))

        // Save button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsSecondaryElement)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.small))

        // Delete button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(LessTheme.radius.small))
                .background(LessTheme.colors.elementsSecondaryElement)
        )
    }
}
