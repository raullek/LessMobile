package az.less.designsystem.base

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Elevation {
    val xxxSmall: Dp = 2.dp
    val xxSmall: Dp = 4.dp
    val xSmall: Dp = 8.dp
    val small: Dp = 12.dp
    val medium: Dp = 16.dp
    val large: Dp = 24.dp
}

val LocalElevation = staticCompositionLocalOf { Elevation }