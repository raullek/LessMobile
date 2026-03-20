package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class FilterOptionsDto(
    val categories: List<FilterCategoryDto> = emptyList(),
    val tags: List<FilterTagDto> = emptyList(),
    val pickupRanges: List<FilterPickupRangeDto> = emptyList(),
    val bagTypes: List<FilterBagTypeDto> = emptyList()
)

@Serializable
data class FilterCategoryDto(
    val id: String,
    val value: String,
    val title: String,
    val imageUrl: String? = null,
    val sortOrder: Int = 0
)

@Serializable
data class FilterTagDto(
    val id: String,
    val value: String,
    val title: String,
    val imageUrl: String? = null,
    val sortOrder: Int = 0
)

@Serializable
data class FilterPickupRangeDto(
    val value: String,
    val title: String
)

@Serializable
data class FilterBagTypeDto(
    val key: String,
    val value: String
)
