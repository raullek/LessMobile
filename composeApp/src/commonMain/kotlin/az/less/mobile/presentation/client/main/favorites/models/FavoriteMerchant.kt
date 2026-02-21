package az.less.mobile.presentation.client.main.favorites.models

data class FavoriteMerchant(
    val id: String,
    val merchantName: String,
    val address: String,
    val imageUrl: String? = null,
    val merchantLogoUrl: String? = null,
    val rating: Float = 0f,
    val distance: String,
    val itemsOnSale: Int = 0,
    val badgeText: String? = null
)
