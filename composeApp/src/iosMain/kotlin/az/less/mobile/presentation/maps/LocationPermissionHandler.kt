package az.less.mobile.presentation.maps

import androidx.compose.runtime.Composable

/**
 * iOS implementation of location permission handler
 * 
 * On iOS, permissions are handled by the system when location services are first accessed.
 * The Info.plist keys (NSLocationWhenInUseUsageDescription) will trigger the permission dialog.
 */
@Composable
actual fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    content: @Composable () -> Unit
) {
    // On iOS, permissions are requested automatically by the system
    // when location services are accessed, so we just render the content
    content()
}





