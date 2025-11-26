package az.less.designsystem.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme

/**
 * Data class representing a segment option
 */
data class SegmentOption(
    val id: String,
    val text: String
)

/**
 * Reusable SegmentedButton component for switching between multiple options
 * Based on modern Jetpack Compose patterns with smooth animations
 * Features:
 * - Minimal smooth animation when switching segments
 * - 8dp horizontal, 4dp vertical padding between container and buttons
 * - Fully reusable with any number of segments
 * - Follows design system patterns
 * 
 * @param options List of segment options to display
 * @param selectedOptionId ID of the currently selected option
 * @param onOptionSelected Callback when an option is selected
 * @param modifier Modifier to be applied to the container
 * @param selectedTextColor Color for the selected button text (default: textIconsNested/white)
 */
@Composable
fun SegmentedButton(
    options: List<SegmentOption>,
    selectedOptionId: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedTextColor: Color = LessTheme.colors.textIconsNested
) {
    require(options.isNotEmpty()) { "SegmentedButton must have at least one option" }
    require(options.any { it.id == selectedOptionId }) { 
        "selectedOptionId must match one of the option IDs" 
    }
    
    val density = LocalDensity.current
    val selectedIndex = options.indexOfFirst { it.id == selectedOptionId }
    
    // Track button widths for animation
    val buttonWidths = remember { mutableStateOf(List(options.size) { 0.dp }) }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(LessTheme.size.xxLarge) // 48dp height
            .shadow(
                elevation = LessTheme.elevation.small,
                shape = RoundedCornerShape(100.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(100.dp))
            .background(LessTheme.colors.elementsPrimaryElement)
            .padding(
                horizontal = LessTheme.spacing.xSmall, // 8dp horizontal padding
                vertical = LessTheme.spacing.xxSmall // 4dp vertical padding
            )
            .selectableGroup()
    ) {
        // Calculate indicator offset: sum of all previous button widths
        val indicatorOffset = if (selectedIndex >= 0 && buttonWidths.value.isNotEmpty()) {
            var offset = 0.dp
            for (i in 0 until selectedIndex) {
                if (i < buttonWidths.value.size && buttonWidths.value[i] > 0.dp) {
                    offset += buttonWidths.value[i]
                }
            }
            offset
        } else {
            0.dp
        }
        
        val indicatorWidth = if (selectedIndex >= 0 && buttonWidths.value.size > selectedIndex && buttonWidths.value[selectedIndex] > 0.dp) {
            buttonWidths.value[selectedIndex]
        } else {
            0.dp
        }
        
        val animatedOffset by animateDpAsState(
            targetValue = indicatorOffset,
            animationSpec = tween(durationMillis = 200), // Minimal animation
            label = "segment_animation"
        )
        
        // Background indicator (rendered FIRST, so it's behind the text)
        if (indicatorWidth > 0.dp) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .width(indicatorWidth)
                        .fillMaxHeight()
                        .offset(x = animatedOffset)
                        .clip(RoundedCornerShape(20.dp)) // 20dp corner radius per design
                        .background(LessTheme.colors.textIconsBrand)
                        .shadow(
                            elevation = LessTheme.elevation.small,
                            shape = RoundedCornerShape(20.dp), // 20dp corner radius per design
                            ambientColor = Color.Black.copy(alpha = 0.06f),
                            spotColor = Color.Black.copy(alpha = 0.06f)
                        )
                )
            }
        }
        
        // Render buttons AFTER indicator (so text appears on top)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            options.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .onGloballyPositioned { coordinates ->
                            with(density) {
                                val widths = buttonWidths.value.toMutableList()
                                widths[index] = coordinates.size.width.toDp()
                                buttonWidths.value = widths
                            }
                        }
                ) {
                    SegmentedButtonItem(
                        text = option.text,
                        isSelected = option.id == selectedOptionId,
                        onClick = { onOptionSelected(option.id) },
                        selectedTextColor = selectedTextColor,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * Individual segment item within SegmentedButton
 */
@Composable
private fun SegmentedButtonItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedTextColor: Color,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Box(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null // No ripple/hint effect
            )
            .padding(
                horizontal = LessTheme.spacing.xSmall + LessTheme.spacing.xxxSmall, // 10dp horizontal (8dp + 2dp)
                vertical = if (isSelected) {
                    LessTheme.spacing.xxxSmall // 2dp vertical for selected
                } else {
                    3.dp // 3dp vertical for unselected
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = LessTheme.typography.body14Semibold,
            color = if (isSelected) {
                selectedTextColor
            } else {
                LessTheme.colors.textIconsBlack
            },
            textAlign = TextAlign.Center
        )
    }
}
