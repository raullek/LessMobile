package az.less.designsystem.base

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


object LessTheme {
    val colors: ColorTokens
        @Composable
        get() = LocalColors.current

    val primitives = Primitives

    val typography: az.less.designsystem.base.Typography
        @Composable
        get() = LocalTypography.current

    val spacing: Spacing
        @Composable
        get() = LocalSpacing.current

    val radius: Radius
        @Composable
        get() = LocalRadius.current

    val elevation: Elevation
        @Composable
        get() = LocalElevation.current

    val size: Size
        @Composable
        get() = LocalSize.current
}


@Composable
fun LessTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) LightPalette else
        LightPalette

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides LessTheme.typography,
        LocalSpacing provides LessTheme.spacing,
        LocalRadius provides LessTheme.radius,
        LocalElevation provides LessTheme.elevation,
        LocalSize provides LessTheme.size,
    ) {
        content()
    }
}
