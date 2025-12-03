package az.less.mobile.presentation.main.explore.models

/**
 * Dynamic filter option that comes from backend
 */
data class DynamicFilterOption(
    val id: String,
    val displayName: String,
    val isSelected: Boolean = false
)

