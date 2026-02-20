package az.less.mobile.presentation.client.main.offers.models

data class SpecialDiscountItem(
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null
)
