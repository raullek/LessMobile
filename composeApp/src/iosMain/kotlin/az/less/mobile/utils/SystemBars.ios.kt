package az.less.mobile.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

@Composable
actual fun SystemBarsEffect(darkTheme: Boolean) {
    SideEffect {
        StatusBarHolder.controller?.darkContent = !darkTheme
    }
}
