package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.CreateReviewRequest
import az.less.mobile.data.remote.model.CreateReviewResponseDto
import az.less.mobile.data.remote.model.MerchantProfileDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class MerchantDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getMerchantProfile(
        merchantId: String,
        includeReviews: Boolean = false,
        reviewsLimit: Int? = null,
        includeOffers: Boolean = false,
        offersLimit: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): NetworkResult<MerchantProfileDto> {
        return safeApiCall {
            httpClient.get("v1/venues/$merchantId") {
                if (includeReviews) parameter("includeReviews", true)
                reviewsLimit?.let { parameter("reviewsLimit", it) }
                if (includeOffers) parameter("includeOffers", true)
                offersLimit?.let { parameter("offersLimit", it) }
                latitude?.let { parameter("latitude", it) }
                longitude?.let { parameter("longitude", it) }
            }
        }
    }

    suspend fun createReview(
        venueId: String,
        request: CreateReviewRequest
    ): NetworkResult<CreateReviewResponseDto> {
        return safeApiCall {
            httpClient.post("v1/merchants/$venueId/reviews") {
                setBody(request)
            }
        }
    }
}
