package az.less.mobile.presentation.reserve.models

/**
 * Reserve information displayed in the info bottom sheet
 */
data class ReserveInfo(
    val venueName: String,
    val pickupTime: String,
    val reserveNumber: String,
    val date: String,
    val pricePerPiece: Double,
    val serviceFee: Double,
    val subtotal: Double
)


