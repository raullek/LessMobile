package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Offers screen response from API
 * GET /api/v1/offers/home
 */
@Serializable
data class OffersScreenDto(
    val categories: List<CategoryDto>,
    val specialCategories: List<SpecialCategoryDto>,
    val segmentedCategories: List<SegmentedCategoryDto>,
    val offerSections: List<OfferSectionDto>
)

@Serializable
data class CategoryDto(
    val id: String,
    val type: String,
    val title: String,
    val imageUrl: String? = null
)

@Serializable
data class SpecialCategoryDto(
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null
)

@Serializable
data class SegmentedCategoryDto(
    val id: String,
    val type: String,
    val title: String
)

@Serializable
data class OfferSectionDto(
    val id: String,
    val type: String,
    val title: String,
    val offers: List<OfferDto>
)

@Serializable
data class OfferDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageBgColor: String? = null,
    val quantity: Int,
    val originalPrice: Double,
    val currentPrice: Double,
    val bagType: String,
    val category: String,
    val pickupTime: String,
    val merchant: MerchantDto
)

@Serializable
data class MerchantDto(
    val id: String,
    val name: String,
    val logoUrl: String? = null,
    val location: String,
    val rating: Double
)
