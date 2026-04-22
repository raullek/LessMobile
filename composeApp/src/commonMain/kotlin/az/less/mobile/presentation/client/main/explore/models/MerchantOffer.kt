package az.less.mobile.presentation.client.main.explore.models

import az.less.mobile.presentation.maps.models.LatLong

/**
 * Represents a merchant offer with location and available slots
 * This is what the backend returns
 */
data class MerchantOffer(
    val merchantId: String,
    val merchantName: String,
    val merchantLogoUrl: String? = null,
    val coordinates: LatLong,
    val slots: List<az.less.mobile.presentation.client.main.explore.models.OfferSlot> = emptyList()
)

/**
 * Represents an individual offer slot (available time/offer)
 */
data class OfferSlot(
    val id: String,
    val title: String,
    val price: String, // e.g. "12.99"
    val pickupTime: String, // e.g. "17:00 - 18:00"
    val imageUrl: String? = null,
    val venueLogoUrl: String? = null,
    val venueName: String? = null
)

