package az.less.mobile.presentation.client.main.offers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import az.less.designsystem.base.LessTheme


@Composable
fun FilterCategoryItem(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector?=null,
    iconTint: Color = LessTheme.colors.textIconsBrand,
    isSelected: Boolean = false,
    onItemClick: (String) -> Unit) {
    val roundedShape = RoundedCornerShape(LessTheme.radius.small)
    
    val backgroundColor = if (isSelected) {
        LessTheme.colors.elementsSecondaryBrand
    } else {
        LessTheme.colors.backgroundPrimary
    }
    
    Box(
        modifier = modifier
            .clip(roundedShape)
            .background(backgroundColor)
            .clickable(
                onClick = { onItemClick(text) }
            )
        ,
        contentAlignment = Alignment.Center
    ) {
        Row (modifier = Modifier.padding(horizontal = LessTheme.radius.small,
            vertical = LessTheme.radius.xSmall)){
            if (icon != null) {
                Icon(
                    modifier = Modifier.size(LessTheme.size.xSmall),
                    imageVector = icon,
                    contentDescription = text,
                    tint = iconTint
                )
            }

            Text(
                modifier = Modifier.padding(start = LessTheme.radius.xSmall),
                style = LessTheme.typography.body14Regular,
                text = text
            )
        }
    }
}