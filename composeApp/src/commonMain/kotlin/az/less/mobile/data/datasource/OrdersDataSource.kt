package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.MerchantOrdersData
import az.less.mobile.data.remote.model.OrderHistoryDataDto
import az.less.mobile.data.remote.model.OrdersDataDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class OrdersDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getOrders(
        type: String,
        page: Int = 1,
        limit: Int = 20
    ): NetworkResult<OrdersDataDto> {
        return safeApiCall {
            httpClient.get("v1/client/orders") {
                parameter("type", type)
                parameter("page", page)
                parameter("limit", limit)
            }
        }
    }

    suspend fun getMerchantOrders(venueId: String): NetworkResult<MerchantOrdersData> {
        return safeApiCall {
            httpClient.get("v1/orders/merchant-orders") {
                parameter("venueId", venueId)
            }
        }
    }

    suspend fun getOrderHistory(
        page: Int = 1,
        limit: Int = 50,
        startDate: String? = null,
        endDate: String? = null
    ): NetworkResult<OrderHistoryDataDto> {
        return safeApiCall {
            httpClient.get("v1/orders/history") {
                parameter("page", page)
                parameter("limit", limit)
                startDate?.let { parameter("startDate", it) }
                endDate?.let { parameter("endDate", it) }
            }
        }
    }
}
