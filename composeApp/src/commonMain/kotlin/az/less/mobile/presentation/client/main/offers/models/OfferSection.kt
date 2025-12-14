package az.less.mobile.presentation.client.main.offers.models

/**
 * Represents a section of offers with title and items
 * Examples: "Top rated", "Top picks for late dinner", etc.
 */
data class OfferSection(
    val id: String,
    val title: String,
    val items: List<az.less.mobile.presentation.client.main.offers.models.OfferItem>,
    val showSeeAll: Boolean = true
)

