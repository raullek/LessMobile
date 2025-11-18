package az.less.mobile

import androidx.compose.runtime.Composable
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        LessTheme {
            AppNavigation()
        }
    }
}