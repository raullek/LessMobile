package az.less.mobile.data.datasource

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
}
