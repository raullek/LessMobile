package az.less.mobile.presentation.client.main.offers.models

/**
 * Represents a section of offers with title and items
 * Maps from API's specialSegments
 */
data class OfferSection(
    val id: String,
    val title: String,
    val offers: List<OfferItem>,
    val searchUrl: String? = null,
    val filters: List<CategoryFilter> = emptyList(),
    val showSeeAll: Boolean = true
) {
    val items: List<OfferItem> get() = offers
}
