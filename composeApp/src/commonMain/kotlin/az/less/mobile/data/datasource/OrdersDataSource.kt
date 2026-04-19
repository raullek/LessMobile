package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.BoughtBoxesResponse
import az.less.mobile.data.remote.model.CreatedBoxesResponse
import az.less.mobile.data.remote.model.OrderDeliveryResponse
import az.less.mobile.data.remote.model.OrderHistoryDataDto
import az.less.mobile.data.remote.model.OrdersDataDto
import az.less.mobile.network.ApiResponse
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class OrdersDataSource(
    private val httpClient: HttpClient
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

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

    suspend fun getBoughtBoxes(): NetworkResult<BoughtBoxesResponse> {
        return safeApiCall {
            httpClient.get("v1/orders/merchant-orders/bought-boxes")
        }
    }

    suspend fun getCreatedBoxes(): NetworkResult<CreatedBoxesResponse> {
        return safeApiCall {
            httpClient.get("v1/orders/merchant-orders/created-boxes")
        }
    }

    /**
     * SSE stream for real-time bought boxes updates.
     * Uses Ktor SSE plugin to receive server-sent events.
     */
    fun streamBoughtBoxes(): Flow<BoughtBoxesResponse> = flow {
        httpClient.sse("v1/orders/merchant-orders/stream") {
            incoming.collect { event ->
                val data = event.data ?: return@collect
                try {
                    val apiResponse = json.decodeFromString<ApiResponse<BoughtBoxesResponse>>(data)
                    if (apiResponse.isSuccess && apiResponse.data != null) {
                        emit(apiResponse.data)
                    }
                } catch (_: Exception) {
                    // Skip malformed events
                }
            }
        }
    }

    suspend fun deliverOrder(orderId: String): NetworkResult<OrderDeliveryResponse> {
        return safeApiCall {
            httpClient.post("v1/orders/$orderId/deliver")
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
