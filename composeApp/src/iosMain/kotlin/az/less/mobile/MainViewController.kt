package az.less.mobile

import androidx.compose.ui.window.ComposeUIViewController
import az.less.mobile.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController(configure = {
        initKoin {}
    }) { App() }
}