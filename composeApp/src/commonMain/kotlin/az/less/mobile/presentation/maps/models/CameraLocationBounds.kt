package az.less.mobile.presentation.maps.models

/**
 * Represents geographical bounds for the camera view
 */
data class CameraLocationBounds(
    val southwest: LatLong,
    val northeast: LatLong,
    val padding: Int = 50
)




