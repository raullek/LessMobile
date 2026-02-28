package az.less.mobile.data.repository

import az.less.mobile.data.datasource.MerchantDataSource
import az.less.mobile.data.remote.model.MerchantProfileDto
import az.less.mobile.data.remote.model.VenueReviewItemDto
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
        latitude: Double?,
        longitude: Double?
    ): NetworkResult<MerchantProfile> {
        return merchantDataSource.getMerchantProfile(
            merchantId = merchantId,
            includeReviews = includeReviews,
            reviewsLimit = reviewsLimit,
            latitude = latitude,
            longitude = longitude
        ).map { it.toDomain() }
    }
}

private fun MerchantProfileDto.toDomain() = MerchantProfile(
    id = id,
    name = name,
    businessName = businessName ?: name,
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
    reviews = reviews?.data?.map { it.toDomain() } ?: emptyList(),
    reviewsTotal = reviews?.total ?: 0,
    reviewsHasMore = reviews?.hasMore ?: false,
    status = status ?: ""
)

private fun VenueReviewItemDto.toDomain() = MerchantReview(
    id = id,
    rating = rating,
    comment = comment ?: "",
    createdAt = createdAt ?: "",
    clientName = client?.name ?: "",
    clientAvatar = client?.avatar,
    replyText = reply?.text,
    replyDate = reply?.repliedAt,
    replyVenueLogo = reply?.venueLogo
)
