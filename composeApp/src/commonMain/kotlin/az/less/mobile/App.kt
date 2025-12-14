package az.less.mobile

import androidx.compose.runtime.Composable
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.AppClientRootScreen
import az.less.mobile.navigation.AppRootNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        LessTheme {
            AppRootNavigation()
        }
    }
}