package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesResponseDto(
    val data: List<CategoryItemDto> = emptyList()
)

@Serializable
data class CategoryItemDto(
    val id: String,
    val value: String,
    val title: String,
    val imageUrl: String? = null,
    val sortOrder: Int = 0,
    val isActive: Boolean = true
)
