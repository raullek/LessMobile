package az.less.mobile.presentation.client.main.offers.models

import org.jetbrains.compose.resources.DrawableResource

data class SegmentedCategory(
    val id: String,
    val type: String,
    val title: String,
    val icon: DrawableResource? = null,
    val iconTint: Long? = null
)
