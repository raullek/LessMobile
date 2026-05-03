package az.less.mobile.presentation.client.main.orders.models

data class Order(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val pickupTimeFormatted: String = "",
    val date: String = "",
    val price: String,
    val pricePerPiece: Double = 0.0,
    val serviceFee: Double = 0.0,
    val subtotal: String,
    val subtotalAmount: Double = 0.0,
    val quantity: Int = 1,
    val reserveNumber: String = "",
    val isCompleted: Boolean = false,
    val completedDate: String? = null,
    /** Id of the venue this order belongs to — used to deep-link into the
     *  venue preview screen from the "Show me location" action. */
    val venueId: String = ""
)
