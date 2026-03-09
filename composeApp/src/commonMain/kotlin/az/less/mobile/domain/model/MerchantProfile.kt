package az.less.mobile.domain.model

data class MerchantProfile(
    val id: String,
    val name: String,
    val businessName: String,
    val businessAddress: String,
    val businessDescription: String,
    val businessLogo: String?,
    val coverImage: String?,
    val lotImage: String?,
    val phone: String,
    val email: String,
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Double?,
    val rating: Float,
    val ratingCount: Int,
    val ratingDistribution: Map<Int, Int>,
    val reviews: List<MerchantReview>,
    val reviewsTotal: Int,
    val reviewsHasMore: Boolean,
    val offers: List<MerchantOffer>,
    val status: String,
    val isFavorite: Boolean = false,
    val favoriteId: String? = null
)

data class MerchantOffer(
    val id: String,
    val title: String,
    val description: String?,
    val images: List<String>,
    val originalPrice: Double,
    val discountedPrice: Double,
    val availableItems: Int,
    val pickupTimeStart: String?,
    val pickupTimeEnd: String?,
    val category: String?
)

data class MerchantReview(
    val id: String,
    val rating: Int,
    val comment: String,
    val createdAt: String,
    val clientName: String,
    val clientAvatar: String?,
    val replyText: String?,
    val replyDate: String?,
    val replyVenueLogo: String?
)
