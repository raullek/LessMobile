package az.less.mobile.domain.repository

import androidx.paging.PagingData
import az.less.mobile.presentation.client.main.orders.models.Order
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    fun getOrders(type: String): Flow<PagingData<Order>>
}
