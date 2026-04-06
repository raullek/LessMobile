package az.less.mobile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.AppClientRootScreen
import az.less.mobile.navigation.AppRootNavigation
import az.less.mobile.presentation.theme.ThemeManager
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    KoinContext {
        val themeManager = koinInject<ThemeManager>()
        val isDarkMode by themeManager.isDarkMode.collectAsState()

        LessTheme(darkTheme = isDarkMode) {
            AppRootNavigation()
        }
    }
}