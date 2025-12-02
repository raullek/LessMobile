package az.less.mobile.presentation.main.explore.models

/**
 * Unified filter item model for all filter types in the explore screen
 */
data class FilterItem(
    val id: String,
    val text: String? = null,
    val iconType: FilterIconType = FilterIconType.NONE,
    val isSelected: Boolean = false
)

/**
 * Icon types for filter items
 */
enum class FilterIconType {
    NONE,      // No icon (text-only filters)
    FILTER,    // Filter icon (for filter button)
    HEART      // Heart icon (for liked button)
}

