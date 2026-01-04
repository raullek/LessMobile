package az.less.mobile.presentation.client.main.saved.models

/**
 * Represents a saved/favorite merchant
 */
data class SavedMerchant(
    val id: String,
    val merchantName: String,
    val address: String,
    val imageUrl: String? = null,
    val merchantLogoUrl: String? = null,
    val rating: Float = 0f,
    val distance: String, // e.g. "1.2 km"
    val itemsOnSale: Int = 0 // 0 means "No active offer", >0 shows "X items on sale"
)
