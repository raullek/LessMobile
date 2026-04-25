package az.less.mobile.domain.repository

import androidx.paging.PagingData
import az.less.mobile.data.remote.model.BoughtBoxesResponse
import az.less.mobile.data.remote.model.CreatedBoxesResponse
import az.less.mobile.data.remote.model.OrderDeliveryResponse
import az.less.mobile.data.remote.model.OrderHistoryDataDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.presentation.client.main.orders.models.Order
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    fun getOrders(type: String): Flow<PagingData<Order>>
    suspend fun getBoughtBoxes(): NetworkResult<BoughtBoxesResponse>
    suspend fun getCreatedBoxes(): NetworkResult<CreatedBoxesResponse>
    fun streamBoughtBoxes(): Flow<BoughtBoxesResponse>
    suspend fun deliverOrder(orderId: String): NetworkResult<OrderDeliveryResponse>
    suspend fun getOrderHistory(
        page: Int = 1,
        limit: Int = 50,
        startDate: String? = null,
        endDate: String? = null,
        venueId: String? = null
    ): NetworkResult<OrderHistoryDataDto>
}
