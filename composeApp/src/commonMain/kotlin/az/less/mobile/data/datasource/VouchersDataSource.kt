package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.VouchersDataDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class VouchersDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getVouchers(
        venueId: String? = null,
        categoryId: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): NetworkResult<VouchersDataDto> {
        return safeApiCall {
            httpClient.get("v1/vouchers") {
                venueId?.let { parameter("venueId", it) }
                categoryId?.let { parameter("categoryId", it) }
                parameter("page", page)
                parameter("limit", limit)
            }
        }
    }
}
