package az.less.mobile.presentation.client.reserve.models

/**
 * Payload for
 * [az.less.mobile.presentation.client.reserve.PreviousOrderInfoBottomSheet] —
 * the *completed/picked-up* order info sheet, whose actions are "Leave review"
 * and "Contact with support".
 *
 * Kept distinct from [ReserveInfo] because the two sheets are responsible for
 * different user actions and therefore evolve independently (review submission,
 * support flow, etc.).
 */
data class PreviousOrderInfo(
    /** Box title shown as the sheet's main title — e.g. "Small Surprise Bag". */
    val boxTitle: String,
    /** Brand-green status line under the title, e.g. "Picked up on 12 May". */
    val pickedUpOn: String,
    val reserveNumber: String,
    val date: String,
    val pricePerPiece: Double,
    val serviceFee: Double,
    val subtotal: Double,
    /** Order id, passed to the review/support flows. */
    val orderId: String = "",
    /** Venue id — needed for `POST /v1/merchants/{venueId}/reviews`. */
    val venueId: String = ""
)
