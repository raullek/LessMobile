package az.less.mobile.presentation.maps.models

/**
 * Represents a marker on the map
 */
data class Marker(
    val id: String,
    val position: LatLong,
    val itemsCount: Int = 0,
    val title: String? = null,
    val snippet: String? = null,
    val iconUrl: String? = null,
    val tag: Any? = null,
    val isVisible: Boolean = true,
    val isSelected: Boolean = false,
    val alpha: Float = 1f,
    val rotation: Float = 0f,
    val isDraggable: Boolean = false
)





