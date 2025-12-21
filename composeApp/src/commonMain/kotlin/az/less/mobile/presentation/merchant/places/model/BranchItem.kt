package az.less.mobile.presentation.merchant.places.model

/**
 * Model representing a merchant branch/place
 */
data class BranchItem(
    val id: String,
    val name: String,
    val address: String,
    val phone: String,
    val imageUrl: String? = null,
    val itemsOnSale: Int = 0,
    val hasActiveDiscount: Boolean = false,
    val rating: Float = 0f,
    val distance: String = ""
)
