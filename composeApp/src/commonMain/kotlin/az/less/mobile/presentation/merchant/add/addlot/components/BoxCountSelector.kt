package az.less.mobile.presentation.merchant.add.addlot.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_minus_24dp
import lessmobile.composeapp.generated.resources.ic_plus_24dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun BoxCountSelector(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    minValue: Int = 1
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxLarge, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Decrement button
        Box(
            modifier = Modifier
                .size(LessTheme.size.xxLarge)
                .shadow(
                    elevation = LessTheme.elevation.medium,
                    shape = RoundedCornerShape(LessTheme.radius.medium),
                    clip = false
                )
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(color = LessTheme.colors.backgroundPrimary)
                .clickable(enabled = count > minValue) { onDecrement() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_minus_24dp),
                contentDescription = "Decrease",
                tint = if (count > minValue) {
                    LessTheme.colors.elementsPrimaryBrand
                } else {
                    LessTheme.colors.textIconsGrey
                },
                modifier = Modifier.size(LessTheme.size.medium)
            )
        }

        // Count display
        Text(
            text = count.toString(),
            style = LessTheme.typography.display48Medium,
            color = LessTheme.colors.textIconsBlack
        )

        // Increment button
        Box(
            modifier = Modifier
                .size(LessTheme.size.xxLarge)
                .shadow(
                    elevation = LessTheme.elevation.medium,
                    shape = RoundedCornerShape(LessTheme.radius.medium),
                    clip = false
                )
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(color = LessTheme.colors.backgroundPrimary)
                .clickable { onIncrement() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_plus_24dp),
                contentDescription = "Increase",
                tint = LessTheme.colors.elementsPrimaryBrand,
                modifier = Modifier.size(LessTheme.size.medium)
            )
        }
    }
}

