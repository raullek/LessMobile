package az.less.mobile.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import az.less.mobile.data.datasource.OrdersDataSource
import az.less.mobile.data.remote.model.OrderItemDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.presentation.client.main.orders.models.Order
import az.less.mobile.utils.extractTime

class OrdersPagingSource(
    private val dataSource: OrdersDataSource,
    private val type: String
) : PagingSource<Int, Order>() {

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val page = params.key ?: 1

        return when (val result = dataSource.getOrders(type, page, params.loadSize)) {
            is NetworkResult.Success -> {
                val data = result.data
                val orders = data.data.map { it.toDomain() }
                val pagination = data.pagination

                LoadResult.Page(
                    data = orders,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (pagination?.hasNext == true) page + 1 else null
                )
            }

            is NetworkResult.Error -> {
                LoadResult.Error(Exception(result.error.message))
            }

            is NetworkResult.Loading -> {
                LoadResult.Error(Exception("Unexpected loading state"))
            }
        }
    }
}

private fun Double.formatAmount(): String {
    val whole = toLong()
    val truncatedWhole = if (whole > 99999) 99999 else whole
    val fraction = ((this - whole) * 100).toLong()
    return if (fraction == 0L) "$truncatedWhole" else "$truncatedWhole.${fraction.toString().padStart(2, '0')}"
}

private fun OrderItemDto.toDomain() = Order(
    id = id,
    title = item?.name ?: venue?.name ?: "",
    imageUrl = item?.image,
    pickupTimeStart = pickupTimeStart?.extractTime(),
    pickupTimeEnd = pickupTimeEnd?.extractTime(),
    pickupTimeFormatted = pickupTimeFormatted ?: "",
    date = dateFormatted ?: "",
    price = price.formatAmount(),
    pricePerPiece = pricePerPiece,
    serviceFee = serviceFee,
    subtotal = subtotal.formatAmount(),
    subtotalAmount = subtotal,
    quantity = quantity,
    reserveNumber = reserveNumber ?: orderNumber ?: "",
    isCompleted = status == "completed" || status == "picked_up",
    completedDate = completedAt
)
