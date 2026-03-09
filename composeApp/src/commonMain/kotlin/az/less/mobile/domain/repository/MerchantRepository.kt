package az.less.mobile.domain.repository

import az.less.mobile.domain.model.MerchantProfile
import az.less.mobile.network.NetworkResult

interface MerchantRepository {
    suspend fun getMerchantProfile(
        merchantId: String,
        includeReviews: Boolean = false,
        reviewsLimit: Int? = null,
        includeOffers: Boolean = false,
        offersLimit: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): NetworkResult<MerchantProfile>
}
