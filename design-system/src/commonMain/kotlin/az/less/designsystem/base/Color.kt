package az.less.designsystem.base

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ========== Primitive Colors ==========

object Primitives {
    // LessGreen
    val LessGreenL50 = Color(0xFFFCFFF9)
    val LessGreenL100 = Color(0xFFE0FCC0)
    val LessGreenL200 = Color(0xFFCFFAAC)
    val LessGreenL300 = Color(0xFFBEF98D)
    val LessGreenL400 = Color(0xFF94E08A)
    val LessGreenL500 = Color(0xFF76CA83)
    val LessGreenL600 = Color(0xFF46A566)
    val LessGreenL700 = Color(0xFF3A9062)
    val LessGreenL800 = Color(0xFF2D7043)
    val LessGreenL900 = Color(0xFF1F5027)
    val LessGreenL950 = Color(0xFF15260D)

    // SkyBlue
    val SkyBlueS50 = Color(0xFFF4FDFF)
    val SkyBlueS100 = Color(0xFFD8F4FD)
    val SkyBlueS200 = Color(0xFFA1E1F6)
    val SkyBlueS300 = Color(0xFF6FC3E6)
    val SkyBlueS400 = Color(0xFF5BACD6)
    val SkyBlueS500 = Color(0xFF4995C6)
    val SkyBlueS600 = Color(0xFF337DB6)
    val SkyBlueS700 = Color(0xFF2F6385)
    val SkyBlueS800 = Color(0xFF23495C)
    val SkyBlueS900 = Color(0xFF172F39)
    val SkyBlueS950 = Color(0xFF102228)

    // Warning
    val Warning50 = Color(0xFFFFFBF0)
    val Warning100 = Color(0xFFFFF5D6)
    val Warning200 = Color(0xFFF7E2A1)
    val Warning300 = Color(0xFFF2CE63)
    val Warning400 = Color(0xFFF0C441)
    val Warning500 = Color(0xFFFF8E1C)
    val Warning600 = Color(0xFFD8A611)
    val Warning700 = Color(0xFFA7800E)
    val Warning800 = Color(0xFF755A0A)
    val Warning900 = Color(0xFF4A3906)
    val Warning950 = Color(0xFF342804)

    // Success
    val Success50 = Color(0xFFF4FFF7)
    val Success100 = Color(0xFFD6FFE0)
    val Success200 = Color(0xFF9CFCB2)
    val Success300 = Color(0xFF67EE86)
    val Success400 = Color(0xFF51E072)
    val Success500 = Color(0xFF3DD260)
    val Success600 = Color(0xFF28C14B)
    val Success700 = Color(0xFF1D9739)
    val Success800 = Color(0xFF146A28)
    val Success900 = Color(0xFF0C4319)
    val Success950 = Color(0xFF082F12)

    // Red
    val RedR50 = Color(0xFFFFF4F4)
    val RedR100 = Color(0xFFFFD6D6)
    val RedR200 = Color(0xFFFC9C9C)
    val RedR300 = Color(0xFFEE6767)
    val RedR400 = Color(0xFFE05151)
    val RedR500 = Color(0xFFD23D3D)
    val RedR600 = Color(0xFFC12828)
    val RedR700 = Color(0xFF971D1D)
    val RedR800 = Color(0xFF621D1D)
    val RedR900 = Color(0xFF3D1313)
    val RedR950 = Color(0xFF281010)

    // Base (Neutral)
    val BaseB0 = Color(0xFFFFFFFF)
    val BaseB50 = Color(0xFFF4F7F6)
    val BaseB100 = Color(0xFFEAEAEA)
    val BaseB200 = Color(0xFFC6CAC7)
    val BaseB300 = Color(0xFFA9AFAB)
    val BaseB400 = Color(0xFF8F9893)
    val BaseB500 = Color(0xFF757E79)
    val BaseB600 = Color(0xFF5F6862)
    val BaseB700 = Color(0xFF47504B)
    val BaseB800 = Color(0xFF343B37)
    val BaseB900 = Color(0xFF1F2522)
    val BaseB950 = Color(0xFF171A1C)
}

// ========== Light Mode Colors ==========

object LightColors {
    val TextIconsNested = Color(0xFFFFFFFF)
    val TextIconsBlack = Color(0xFF171A1C)
    val TextIconsGrey = Color(0xFF5F6862)
    val TextIconsSecondary = Color(0xFF9A9A9A)
    val TextIconsThird = Color(0xFFA9AFAB)
    val TextIconsBrand = Color(0xFF31BC5C)
    val TextIconsLightBrand = Color(0xFFBEF98D)
    val BackgroundPrimary = Color(0xFFFFFFFF)
    val BackgroundSecond = Color(0xFFF5F7F7)
    val BackgroundBrand = Color(0xFFFCFFF9)
    val ElementsPrimaryElement = Color(0xFFFFFFFF)
    val ElementsSecondaryElement = Color(0xFFF4F7F6)
    val ElementsPrimaryBrand = Color(0xFF31BC5C)
    val ElementsSecondaryBrand = Color(0xFFE0FCC0)
    val ElementPressedPrimaryBrand = Color(0xFF2D7043)
    val ElementPressedSecondaryBrand = Color(0xFFBEF98D)
    val TextIconsError = Color(0xFFE05151)
    val TextIconsSuccess = Color(0xFF28C14B)
    val TextIconsWarning = Color(0xFFFF8E1C)
    val TextIconsInfo = Color(0xFF337DB6)
    val SurfaceError = Color(0xFFFFF4F4)
    val SurfaceWhite = Color(0xFFFFFFFF)
    val SurfaceInfo = Color(0xFFF4FDFF)
    val ElementsThirdElement = Color(0xFFC6CAC7)
    val ElementPressedSecondaryElement = Color(0xFFEAEAEA)
    val ElementPressedThirdElement = Color(0xFFA9AFAB)
    val BorderPrimary = Color(0xFFEAEAEA)
}

// ========== Dark Mode Colors ==========

object DarkColors {
    val TextIconsNested = Color(0xFFFFFFFF)
    val TextIconsBlack = Color(0xFFF4F7F6)
    val TextIconsGrey = Color(0xFF5F6862)

    val TextIconsSecondary = Color(0xFF9A9A9A)
    val TextIconsThird = Color(0xFF757E79)
    val TextIconsBrand = Color(0xFF31BC5C)
    val TextIconsLightBrand = Color(0xFFBEF98D)
    val BackgroundPrimary = Color(0xFF1F2522)
    val BackgroundSecond = Color(0xFF171A1C)
    val BackgroundBrand = Color(0xFF15260D)
    val ElementsPrimaryElement = Color(0xFF1F2522)
    val ElementsSecondaryElement = Color(0xFF1F2522)
    val ElementsPrimaryBrand = Color(0xFF46A566)
    val ElementsSecondaryBrand = Color(0xFF1F5027)
    val ElementPressedPrimaryBrand = Color(0xFF94E08A)
    val ElementPressedSecondaryBrand = Color(0xFF3A9062)
    val TextIconsError = Color(0xFFE05151)
    val TextIconsSuccess = Color(0xFF28C14B)
    val TextIconsWarning = Color(0xFFFF8E1C)
    val TextIconsInfo = Color(0xFF337DB6)
    val SurfaceError = Color(0xFF281010)
    val SurfaceWhite = Color(0xFF171A1C)
    val SurfaceInfo = Color(0xFF102228)
    val ElementsThirdElement = Color(0xFF343B37)
    val ElementPressedSecondaryElement = Color(0xFF1F2522)
    val ElementPressedThirdElement = Color(0xFF47504B)
    val BorderPrimary = Color(0xFF343B37)
}

// ========== Theme Color Tokens ==========

data class ColorTokens(
    val textIconsNested: Color,
    val textIconsBlack: Color,
    val textIconsGrey: Color,
    val textIconsSecondary:Color,
    val textIconsThird: Color,
    val textIconsBrand: Color,
    val textIconsLightBrand: Color,
    val backgroundPrimary: Color,
    val backgroundSecond: Color,
    val backgroundBrand: Color,
    val elementsPrimaryElement: Color,
    val elementsSecondaryElement: Color,
    val elementsPrimaryBrand: Color,
    val elementsSecondaryBrand: Color,
    val elementPressedPrimaryBrand: Color,
    val elementPressedSecondaryBrand: Color,
    val textIconsError: Color,
    val textIconsSuccess: Color,
    val textIconsWarning: Color,
    val textIconsInfo: Color,
    val surfaceError: Color,
    val surfaceWhite: Color,
    val surfaceInfo: Color,
    val elementsThirdElement: Color,
    val elementPressedSecondaryElement: Color,
    val elementPressedThirdElement: Color,
    val borderPrimary: Color,
)

val LightPalette = ColorTokens(
    textIconsNested = LightColors.TextIconsNested,
    textIconsBlack = LightColors.TextIconsBlack,
    textIconsGrey = LightColors.TextIconsGrey,
    textIconsThird = LightColors.TextIconsThird,
    textIconsSecondary = LightColors.TextIconsSecondary,
    textIconsBrand = LightColors.TextIconsBrand,
    textIconsLightBrand = LightColors.TextIconsLightBrand,
    backgroundPrimary = LightColors.BackgroundPrimary,
    backgroundSecond = LightColors.BackgroundSecond,
    backgroundBrand = LightColors.BackgroundBrand,
    elementsPrimaryElement = LightColors.ElementsPrimaryElement,
    elementsSecondaryElement = LightColors.ElementsSecondaryElement,
    elementsPrimaryBrand = LightColors.ElementsPrimaryBrand,
    elementsSecondaryBrand = LightColors.ElementsSecondaryBrand,
    elementPressedPrimaryBrand = LightColors.ElementPressedPrimaryBrand,
    elementPressedSecondaryBrand = LightColors.ElementPressedSecondaryBrand,
    textIconsError = LightColors.TextIconsError,
    textIconsSuccess = LightColors.TextIconsSuccess,
    textIconsWarning = LightColors.TextIconsWarning,
    textIconsInfo = LightColors.TextIconsInfo,
    surfaceError = LightColors.SurfaceError,
    surfaceWhite = LightColors.SurfaceWhite,
    surfaceInfo = LightColors.SurfaceInfo,
    elementsThirdElement = LightColors.ElementsThirdElement,
    elementPressedSecondaryElement = LightColors.ElementPressedSecondaryElement,
    elementPressedThirdElement = LightColors.ElementPressedThirdElement,
    borderPrimary = LightColors.BorderPrimary,
)

val DarkPalette = ColorTokens(
    textIconsNested = DarkColors.TextIconsNested,
    textIconsBlack = DarkColors.TextIconsBlack,
    textIconsGrey = DarkColors.TextIconsGrey,
    textIconsThird = DarkColors.TextIconsThird,
    textIconsBrand = DarkColors.TextIconsBrand,
    textIconsSecondary = DarkColors.TextIconsSecondary,
    textIconsLightBrand = DarkColors.TextIconsLightBrand,
    backgroundPrimary = DarkColors.BackgroundPrimary,
    backgroundSecond = DarkColors.BackgroundSecond,
    backgroundBrand = DarkColors.BackgroundBrand,
    elementsPrimaryElement = DarkColors.ElementsPrimaryElement,
    elementsSecondaryElement = DarkColors.ElementsSecondaryElement,
    elementsPrimaryBrand = DarkColors.ElementsPrimaryBrand,
    elementsSecondaryBrand = DarkColors.ElementsSecondaryBrand,
    elementPressedPrimaryBrand = DarkColors.ElementPressedPrimaryBrand,
    elementPressedSecondaryBrand = DarkColors.ElementPressedSecondaryBrand,
    textIconsError = DarkColors.TextIconsError,
    textIconsSuccess = DarkColors.TextIconsSuccess,
    textIconsWarning = DarkColors.TextIconsWarning,
    textIconsInfo = DarkColors.TextIconsInfo,
    surfaceError = DarkColors.SurfaceError,
    surfaceWhite = DarkColors.SurfaceWhite,
    surfaceInfo = DarkColors.SurfaceInfo,
    elementsThirdElement = DarkColors.ElementsThirdElement,
    elementPressedSecondaryElement = DarkColors.ElementPressedSecondaryElement,
    elementPressedThirdElement = DarkColors.ElementPressedThirdElement,
    borderPrimary = DarkColors.BorderPrimary,
)

val LocalColors = staticCompositionLocalOf { LightPalette }