package az.less.mobile.presentation.client.main.offers.models

import kotlinx.serialization.Serializable

data class Category(
    val id: String,
    val type: String,
    val title: String,
    val imageUrl: String? = null,
    val filters: List<CategoryFilter> = emptyList(),
    val searchUrl: String? = null
)

@Serializable
data class CategoryFilter(
    val searchFilterId: String,
    val values: List<String> = emptyList()
)
