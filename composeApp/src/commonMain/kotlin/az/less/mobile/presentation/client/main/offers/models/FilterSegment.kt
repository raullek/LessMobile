package az.less.mobile.presentation.client.main.offers.models

import org.jetbrains.compose.resources.DrawableResource

data class HomepageButton(
    val id: String,
    val type: String,
    val title: String,
    val searchUrl: String? = null,
    val icon: DrawableResource? = null,
    val iconTint: Long? = null
)
