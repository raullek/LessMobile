package az.less.mobile.presentation.maps.models

/**
 * Represents a location with coordinates and additional metadata
 */
data class Location(
    val latLong: LatLong,
    val accuracy: Float? = null,
    val bearing: Float? = null,
    val speed: Float? = null,
    val timestamp: Long? = null
)




