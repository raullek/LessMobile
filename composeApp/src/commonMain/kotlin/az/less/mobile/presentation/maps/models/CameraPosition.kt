package az.less.mobile.presentation.maps.models

/**
 * Represents the camera position and zoom level
 */
data class CameraPosition(
    val target: LatLong,
    val zoom: Float = 15f,
    val tilt: Float = 0f,
    val bearing: Float = 0f
)


