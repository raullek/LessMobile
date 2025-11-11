package az.less.designsystem.base

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorTokens(
    val primary: Color
    //TODO add other palette colors
)

val LightPalette = ColorTokens(
    primary = Color(0xFF6200EE)
)
val DarkPalette = ColorTokens(
    primary = Color(0xFF6200EE)
)

val LocalColors = staticCompositionLocalOf { LightPalette }