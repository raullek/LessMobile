package az.less.mobile.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import az.less.mobile.data.datasource.OrdersDataSource
import az.less.mobile.data.paging.OrdersPagingSource
import az.less.mobile.data.remote.model.MerchantOrdersData
import az.less.mobile.data.remote.model.OrderHistoryDataDto
import az.less.mobile.domain.repository.OrdersRepository
import az.less.mobile.network.NetworkResult
import az.less.mobile.presentation.client.main.orders.models.Order
import kotlinx.coroutines.flow.Flow

class OrdersRepositoryImpl(
    private val ordersDataSource: OrdersDataSource
) : OrdersRepository {

    override fun getOrders(type: String): Flow<PagingData<Order>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            OrdersPagingSource(
                dataSource = ordersDataSource,
                type = type
            )
        }
    ).flow

    override suspend fun getMerchantOrders(venueId: String): NetworkResult<MerchantOrdersData> {
        return ordersDataSource.getMerchantOrders(venueId)
    }

    override suspend fun getOrderHistory(
        page: Int,
        limit: Int,
        startDate: String?,
        endDate: String?
    ): NetworkResult<OrderHistoryDataDto> {
        return ordersDataSource.getOrderHistory(page, limit, startDate, endDate)
    }
}
