package az.less.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme

/**
 * Custom Radio Button matching the Less app design
 * Based on Figma design with green brand color
 */
@Composable
fun DsRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .then(
                if (onClick != null && enabled) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // Outer circle (border)
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(
                    width = 2.dp,
                    color = if (enabled) {
                        LessTheme.colors.textIconsBrand
                    } else {
                        LessTheme.colors.elementsThirdElement
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Inner filled circle (only visible when selected)
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = if (enabled) {
                                LessTheme.colors.textIconsBrand
                            } else {
                                LessTheme.colors.elementsThirdElement
                            },
                            shape = CircleShape
                        )
                )
            }
        }
    }
}




