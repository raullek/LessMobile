package az.less.mobile.presentation.client.main.explore.models

data class FilterData(
    val categories: List<FilterCategory> = emptyList()
)

data class FilterCategory(
    val id: String,
    val title: String,
    val fieldName: String,
    val isMultiSelect: Boolean,
    val options: List<FilterOption> = emptyList()
)

data class FilterOption(
    val id: String,
    val text: String,
    val isSelected: Boolean = false,
    val imageUrl: String? = null
)

