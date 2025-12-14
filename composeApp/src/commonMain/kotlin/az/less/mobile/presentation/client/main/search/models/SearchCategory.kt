package az.less.mobile.presentation.client.main.search.models

import org.jetbrains.compose.resources.DrawableResource

/**
 * Model for search category items
 */
data class SearchCategory(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val testImage: DrawableResource? = null
)


