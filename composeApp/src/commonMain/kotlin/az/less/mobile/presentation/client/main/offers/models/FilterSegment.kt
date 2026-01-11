package az.less.mobile.presentation.client.main.offers.models

import org.jetbrains.compose.resources.DrawableResource

/**
 * Segmented category type enum
 */
enum class SegmentedCategoryType {
    NEAREST,
    TOP_RATED,
    HOT_DEALS
}

/**
 * Segmented category model for filter items
 */
data class SegmentedCategory(
    val id: String,
    val type: SegmentedCategoryType,
    val title: String,
    val icon: DrawableResource? = null,
    val iconTint: Long? = null // Color as Long (e.g., 0xFFFF8B38)
)

// Keep FilterSegment as alias for backward compatibility
@Deprecated("Use SegmentedCategory instead", ReplaceWith("SegmentedCategory"))
typealias FilterSegment = SegmentedCategory

@Deprecated("Use SegmentedCategoryType instead", ReplaceWith("SegmentedCategoryType"))
typealias FilterSegmentType = SegmentedCategoryType
