package az.less.mobile.presentation.client.main.orders.models

data class CartItem(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val pickupTime: String,
    val price: String,
    val quantity: Int = 1,
    val reserveNumber: String = "",
    val isCompleted: Boolean = false,
    val completedDate: String? = null
)
