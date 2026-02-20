package az.less.mobile.domain.model

import az.less.mobile.presentation.client.main.offers.models.Category
import az.less.mobile.presentation.client.main.offers.models.OfferSection
import az.less.mobile.presentation.client.main.offers.models.SegmentedCategory
import az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem

data class OffersHomeData(
    val categories: List<Category>,
    val specialCategories: List<SpecialDiscountItem>,
    val segmentedCategories: List<SegmentedCategory>,
    val offerSections: List<OfferSection>
)
