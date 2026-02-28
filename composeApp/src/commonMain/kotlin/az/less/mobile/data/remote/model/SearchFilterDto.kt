package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchFilterDto(
    val id: String,
    val title: String,
    val fieldName: String,
    val entityName: String,
    val uiType: String,
    val isMultiSelect: Boolean,
    val options: List<SearchFilterOptionDto> = emptyList()
)

@Serializable
data class SearchFilterOptionDto(
    val key: String,
    val value: String,
    val imageUrl: String? = null
)
