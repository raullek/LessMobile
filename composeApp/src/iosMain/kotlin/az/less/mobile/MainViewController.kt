package az.less.mobile

import androidx.compose.ui.window.ComposeUIViewController
import az.less.mobile.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController(
        configure = {
            initKoin {}
            // Enable edge-to-edge by ensuring the view extends under system bars
            enforceStrictPlistSanityCheck = false
        }
    ) { 
        App() 
    }
}