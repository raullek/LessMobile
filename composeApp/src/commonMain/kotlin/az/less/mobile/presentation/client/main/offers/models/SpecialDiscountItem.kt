package az.less.mobile.presentation.client.main.offers.models

import org.jetbrains.compose.resources.DrawableResource

data class SpecialDiscountItem(
    val id: String,
    val type: String = "DISCOUNT",
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val testImage: DrawableResource? = null // For test images from resources
)
