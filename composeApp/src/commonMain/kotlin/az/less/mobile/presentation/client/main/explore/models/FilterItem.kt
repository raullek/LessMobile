package az.less.mobile.presentation.client.main.explore.models

data class FilterItem(
    val quickFilter: QuickFilter,
    val isSelected: Boolean = false
) {
    val id: String get() = quickFilter.name
    val text: String? get() = quickFilter.displayName
    val iconType: FilterIconType get() = quickFilter.iconType
}

enum class FilterIconType {
    NONE,
    FILTER,
    HEART
}

