package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Item shape returned by `GET /api/v1/search/categories`. The endpoint returns
 * the list directly inside the standard `ApiResponse.data` envelope, so callers
 * deserialize as `List<CategoryItemDto>` (no extra wrapper DTO).
 */
@Serializable
data class CategoryItemDto(
    val id: String,
    val value: String,
    val title: String,
    val name: String? = null,
    val displayTitle: String? = null,
    val category: String? = null,
    val imageUrl: String? = null,
    val sortOrder: Int = 0,
    val isActive: Boolean = true
)
