package az.less.mobile.presentation.client.reserve.models

/**
 * Reserve information displayed in the info bottom sheet
 */
/**
 * Payload for [az.less.mobile.presentation.client.reserve.ReserveInfoBottomSheet]
 * — the *active* order info sheet, whose only action is "Show me location".
 */
data class ReserveInfo(
    /** Box title shown as the sheet's main title — e.g. "Small Surprise Bag". */
    val venueName: String,
    /** Pickup-time hint shown under the title (e.g. "Pickup 18:00 - 19:00"). */
    val pickupTime: String,
    val reserveNumber: String,
    val date: String,
    val pricePerPiece: Double,
    val serviceFee: Double,
    val subtotal: Double,
    /** Used by "Show me location" to deep-link into the in-app venue map. */
    val venueId: String = ""
)


