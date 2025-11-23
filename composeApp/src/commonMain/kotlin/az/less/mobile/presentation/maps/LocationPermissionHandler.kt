package az.less.mobile.presentation.maps

import androidx.compose.runtime.Composable

/**
 * Cross-platform location permission handler
 * 
 * This composable handles location permission requests in a platform-specific way.
 * On Android, it requests runtime permissions.
 * On iOS, permissions are requested when first accessing location services.
 */
@Composable
expect fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {},
    content: @Composable () -> Unit
)




