package az.less.mobile.presentation.main.offers.models

import org.jetbrains.compose.resources.DrawableResource

/**
 * Filter segment type enum
 */
enum class FilterSegmentType {
    NEAREST,
    TOP_RATED,
    HOT_DEALS
}

/**
 * Filter segment model for filter category items
 */
data class FilterSegment(
    val id: String,
    val type: FilterSegmentType,
    val text: String,
    val icon: DrawableResource,
    val iconTint: Long // Color as Long (e.g., 0xFFFF8B38)
)

