package az.less.designsystem.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import lessmobile.design_system.generated.resources.Res
import lessmobile.design_system.generated.resources.SF_Pro
import org.jetbrains.compose.resources.Font

object Typography {
    // SF-Pro Font Family
    val sfProFontFamily: FontFamily
        @Composable
        get() = FontFamily(Font(Res.font.SF_Pro))

    // Display 48 - LH32
    val display48Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 48.sp,
            lineHeight = 32.sp,
            letterSpacing = (-0.1).sp
        )

    // Display 36 - LH32
    val display36Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 32.sp,
            letterSpacing = (-0.1).sp
        )

    val display36Semibold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 36.sp,
            lineHeight = 32.sp,
            letterSpacing = (-0.1).sp
        )

    val display36Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 36.sp,
            lineHeight = 32.sp,
            letterSpacing = (-0.1).sp
        )

    val display36Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 32.sp,
            letterSpacing = (-0.1).sp
        )

    // Title 28 - LH28
    val title28Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.1).sp
        )

    val title28Semibold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.1).sp
        )

    val title28Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 28.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.1).sp
        )

    val title28Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.1).sp
        )

    // Title 24 - LH24
    val title24Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.1).sp
        )

    val title24Semibold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.1).sp
        )

    val title24Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.1).sp
        )

    val title24Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.1).sp
        )

    // Title 20 - LH20
    val title20Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            letterSpacing = (-0.1).sp
        )

    val title20Semibold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            letterSpacing = (-0.1).sp
        )

    val title20Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            letterSpacing = (-0.1).sp,
        )

    val title20Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            letterSpacing = (-0.1).sp
        )

    // Body 16 - LH16
    val body16Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.1).sp
        )

    val body16Semibold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.1).sp
        )

    val body16Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.1).sp
        )

    val body16Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.1).sp
        )

    // Body 14 - LH16
    val body14Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.1).sp
        )

    val body14Semibold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.1).sp
        )

    val body14Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.1).sp
        )

    val body14Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = (-0.1).sp
        )

    // Caption 12 - LH12
    val caption12Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            letterSpacing = (-0.1).sp
        )

    val caption12Semibold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            letterSpacing = (-0.1).sp
        )

    val caption12Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            letterSpacing = (-0.1).sp
        )

    val caption12Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            letterSpacing = (-0.1).sp
        )

    // Caption 10 - LH12
    val caption10Bold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            letterSpacing = (-0.1).sp
        )

    val caption10Semibold: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            letterSpacing = (-0.1).sp
        )

    val caption10Medium: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            letterSpacing = (-0.1).sp
        )

    val caption10Regular: TextStyle
        @Composable
        get() = TextStyle(
            fontFamily = sfProFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            letterSpacing = (-0.1).sp
        )
}

val LocalTypography = staticCompositionLocalOf { Typography }