package az.less.mobile.data.repository

import az.less.mobile.data.datasource.MerchantDataSource
import az.less.mobile.data.remote.model.MerchantProfileDto
import az.less.mobile.data.remote.model.VenueOfferDto
import az.less.mobile.data.remote.model.VenueReviewItemDto
import az.less.mobile.domain.model.MerchantOffer
import az.less.mobile.domain.model.MerchantProfile
import az.less.mobile.domain.model.MerchantReview
import az.less.mobile.domain.repository.MerchantRepository
import az.less.mobile.network.NetworkResult

class MerchantRepositoryImpl(
    private val merchantDataSource: MerchantDataSource
) : MerchantRepository {

    override suspend fun getMerchantProfile(
        merchantId: String,
        includeReviews: Boolean,
        reviewsLimit: Int?,
        includeOffers: Boolean,
        offersLimit: Int?,
        latitude: Double?,
        longitude: Double?
    ): NetworkResult<MerchantProfile> {
        return merchantDataSource.getMerchantProfile(
            merchantId = merchantId,
            includeReviews = includeReviews,
            reviewsLimit = reviewsLimit,
            includeOffers = includeOffers,
            offersLimit = offersLimit,
            latitude = latitude,
            longitude = longitude
        ).map { it.toDomain() }
    }
}

private fun MerchantProfileDto.toDomain() = MerchantProfile(
    id = id.orEmpty(),
    name = name.orEmpty(),
    businessName = businessName.orEmpty(),
    businessAddress = businessAddress ?: "",
    businessDescription = businessDescription ?: "",
    businessLogo = businessLogo,
    coverImage = coverImage,
    lotImage = lotImage,
    phone = phone ?: "",
    email = email ?: "",
    latitude = latitude ?: location?.coordinates?.getOrElse(1) { 0.0 } ?: 0.0,
    longitude = longitude ?: location?.coordinates?.getOrElse(0) { 0.0 } ?: 0.0,
    distanceKm = distanceKm,
    rating = rating?.average?.toFloat() ?: 0f,
    ratingCount = rating?.total ?: 0,
    ratingDistribution = rating?.distribution?.withPercentages
        ?.mapKeys { (key, _) -> key.toIntOrNull() ?: 0 }
        ?.mapValues { (_, value) -> value.count }
        ?: emptyMap(),
    reviews = reviews.map { it.toDomain() },
    reviewsTotal = reviews.size,
    reviewsHasMore = false,
    offers = offers.map { it.toDomain(lotImage) },
    status = status ?: "",
    isFavorite = isFavorite,
    favoriteId = favoriteId
)

private fun VenueOfferDto.toDomain(merchantLotImage: String?) = MerchantOffer(
    id = id.orEmpty(),
    title = title.orEmpty(),
    description = description,
    lotImage = merchantLotImage,
    originalPrice = originalPrice,
    discountedPrice = discountedPrice,
    availableItems = availableItems,
    pickupTimeStart = pickupTimeStart,
    pickupTimeEnd = pickupTimeEnd,
    category = category?.title
)

private fun VenueReviewItemDto.toDomain() = MerchantReview(
    id = id.orEmpty(),
    rating = rating ?: 0,
    comment = comment ?: "",
    createdAt = createdAt ?: "",
    clientName = client?.name ?: "",
    clientAvatar = client?.avatar,
    replyText = reply?.text,
    replyDate = reply?.repliedAt,
    replyVenueLogo = reply?.venueLogo
)
