package az.less.mobile.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import az.less.mobile.data.datasource.OrdersDataSource
import az.less.mobile.data.paging.OrdersPagingSource
import az.less.mobile.domain.repository.OrdersRepository
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
}
