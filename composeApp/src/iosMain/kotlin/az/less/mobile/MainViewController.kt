package az.less.mobile

import androidx.compose.ui.window.ComposeUIViewController
import az.less.mobile.di.initKoin
import az.less.mobile.utils.StatusBarHolder
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController

class AppRootViewController(
    private val child: UIViewController
) : UIViewController(nibName = null, bundle = null) {

    var darkContent: Boolean = true
        set(value) {
            if (field == value) return
            field = value
            setNeedsStatusBarAppearanceUpdate()
        }

    override fun preferredStatusBarStyle(): UIStatusBarStyle =
        if (darkContent) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent

    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLoad() {
        super.viewDidLoad()
        addChildViewController(child)
        child.view.setFrame(view.bounds)
        child.view.setAutoresizingMask(
            UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
        )
        view.addSubview(child.view)
        child.didMoveToParentViewController(this)
    }
}

fun MainViewController(): UIViewController {
    val composeVC = ComposeUIViewController(
        configure = {
            initKoin {}
            enforceStrictPlistSanityCheck = false
        }
    ) {
        App()
    }
    return AppRootViewController(composeVC).also { StatusBarHolder.controller = it }
}
