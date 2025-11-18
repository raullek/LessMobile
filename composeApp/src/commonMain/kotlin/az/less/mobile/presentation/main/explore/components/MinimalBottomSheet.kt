package az.less.mobile.presentation.main.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import kotlin.math.roundToInt

/**
 * Minimal draggable bottom sheet component
 */
@Composable
fun MinimalBottomSheet(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetY by remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    
    Box(
        modifier = modifier
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = LessTheme.radius.large, topEnd = LessTheme.radius.large))
            .background(LessTheme.colors.surfaceWhite)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        // Snap back to original position
                        offsetY = 0f
                    }
                ) { _, dragAmount ->
                    // Allow dragging down only
                    val newOffset = offsetY + dragAmount
                    if (newOffset >= 0) {
                        offsetY = newOffset
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LessTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Drag handle
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(LessTheme.colors.textIconsGrey)
            )
            
            // Content
            content()
        }
    }
}

