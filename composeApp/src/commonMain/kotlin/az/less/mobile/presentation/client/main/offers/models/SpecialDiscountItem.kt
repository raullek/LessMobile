package az.less.mobile.presentation.client.main.offers.models

import org.jetbrains.compose.resources.DrawableResource

data class SpecialDiscountItem(
    val id: String,
    val title: String, // e.g. "Special discount for Desserts 🧁"
    val subtitle: String, // e.g. "Hurry to pick up from 22:00"
    val imageUrl: String? = null,
    val testImage: DrawableResource? = null // For test images from resources
)

