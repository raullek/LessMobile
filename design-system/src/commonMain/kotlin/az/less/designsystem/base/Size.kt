package az.less.designsystem.base

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Size {
    val xxxSmall: Dp = 8.dp
    val xxSmall: Dp = 12.dp
    val xSmall: Dp = 16.dp
    val small: Dp = 20.dp
    val medium: Dp = 24.dp
    val large: Dp = 32.dp
    val xLarge: Dp = 40.dp
    val xxLarge: Dp = 48.dp
    val xxxLarge: Dp = 56.dp
    val huge: Dp = 64.dp
}

val LocalSize = staticCompositionLocalOf { Size }

