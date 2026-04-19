package az.less.mobile.presentation.merchant.add.addlot.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.merchant.add.addlot.model.IconGridOption
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategoryGridItem(
    option: IconGridOption,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
    hasError: Boolean = false
) {
    val backgroundColor = if (isSelected) {
        LessTheme.colors.elementsPrimaryBrand
    } else {
        LessTheme.colors.elementsPrimaryElement
    }

    val textColor = if (isSelected) {
        LessTheme.colors.textIconsNested
    } else {
        LessTheme.colors.textIconsBlack
    }

    val errorModifier = if (hasError && !isSelected) {
        Modifier.border(1.dp, LessTheme.colors.textIconsError, RoundedCornerShape(LessTheme.radius.small))
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(LessTheme.radius.small))
            .then(errorModifier)
            .background(color = backgroundColor)
            .clickable { onSelected() }
            .padding(top = LessTheme.spacing.xxSmall, bottom = LessTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Category image
        Box(
            modifier = Modifier
                .size(LessTheme.size.huge)
                .padding(LessTheme.spacing.xxSmall),
            contentAlignment = Alignment.Center
        ) {
            when {
                option.imageUrl != null -> {
                    AsyncImage(
                        model = option.imageUrl,
                        contentDescription = option.label,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(LessTheme.radius.xSmall)),
                        contentScale = ContentScale.Crop
                    )
                }
                option.icon != null -> {
                    Image(
                        painter = painterResource(option.icon),
                        contentDescription = option.label,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(LessTheme.radius.xSmall)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Text(
            text = option.label,
            style = LessTheme.typography.body14Semibold,
            color = textColor
        )
    }
}
