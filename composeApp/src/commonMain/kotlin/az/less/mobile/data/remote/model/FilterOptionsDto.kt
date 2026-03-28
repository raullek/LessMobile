package az.less.mobile.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterOptionsDto(
    val categories: FilterSectionDto<FilterCategoryDto>? = null,
    val tags: FilterSectionDto<FilterTagDto>? = null,
    val pickupRanges: FilterSectionDto<FilterPickupRangeDto>? = null,
    val bagTypes: FilterSectionDto<FilterBagTypeDto>? = null
)

@Serializable
data class FilterSectionDto<T>(
    val selection: SelectionType? = null,
    val options: List<T>? = null
)

@Serializable
enum class SelectionType {
    @SerialName("single") SINGLE,
    @SerialName("multi") MULTI
}

@Serializable
data class FilterCategoryDto(
    val id: String? = null,
    val value: String? = null,
    val title: String? = null,
    val imageUrl: String? = null,
    val sortOrder: Int? = null
)

@Serializable
data class FilterTagDto(
    val id: String? = null,
    val value: String? = null,
    val title: String? = null,
    val imageUrl: String? = null,
    val sortOrder: Int? = null
)

@Serializable
data class FilterPickupRangeDto(
    val id: String? = null,
    val value: String? = null,
    val title: String? = null,
    val sortOrder: Int? = null
)

@Serializable
data class FilterBagTypeDto(
    val id: String? = null,
    val value: String? = null,
    val title: String? = null,
    val sortOrder: Int? = null
)
