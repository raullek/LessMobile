package az.less.mobile.presentation.main.offers.models

import org.jetbrains.compose.resources.DrawableResource

data class Category(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val testImage: DrawableResource
)

