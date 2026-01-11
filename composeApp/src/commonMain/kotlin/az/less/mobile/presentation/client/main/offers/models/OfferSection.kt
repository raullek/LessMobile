package az.less.mobile.presentation.client.main.offers.models

/**
 * Represents a section of offers with title and items
 * Examples: "Top rated", "Top picks for late dinner", etc.
 */
data class OfferSection(
    val id: String,
    val type: String = "RECOMMENDATION",
    val title: String,
    val offers: List<OfferItem>,
    val showSeeAll: Boolean = true
) {
    // Backward compatibility
    val items: List<OfferItem> get() = offers
}
