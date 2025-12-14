package az.less.mobile.presentation.client.main.explore.models

/**
 * Filter data for the filter bottom sheet
 * Contains filter categories with their options
 */
data class FilterData(
    val categories: List<az.less.mobile.presentation.client.main.explore.models.FilterCategory> = emptyList()
)

/**
 * Filter category with title and options
 */
data class FilterCategory(
    val id: String,
    val title: String,
    val options: List<az.less.mobile.presentation.client.main.explore.models.FilterOption> = emptyList()
)

/**
 * Individual filter option
 */
data class FilterOption(
    val id: String,
    val text: String,
    val isSelected: Boolean = false
)

