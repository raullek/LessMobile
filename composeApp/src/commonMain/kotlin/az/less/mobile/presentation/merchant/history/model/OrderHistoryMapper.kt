package az.less.mobile.presentation.merchant.history.model

import az.less.mobile.data.remote.model.OrderHistoryDataDto

fun OrderHistoryDataDto.toIncomeHistoryList(): List<IncomeHistory> {
    return history
        ?.mapNotNull { group ->
            val date = group.date ?: return@mapNotNull null
            val positions = group.orders
                ?.mapNotNull { order ->
                    IncomePosition(
                        id = order.id ?: return@mapNotNull null,
                        customerName = order.client?.name ?: "",
                        customerInitials = order.client?.initials ?: "",
                        time = order.timeFormatted ?: "",
                        amount = formatAmount(order.amount)
                    )
                }
                ?: emptyList()

            if (positions.isEmpty()) return@mapNotNull null

            IncomeHistory(
                date = date,
                incomeValue = formatAmount(group.totalAmount),
                incomePositions = positions
            )
        }
        ?: emptyList()
}

private fun formatAmount(amount: Double?): String {
    if (amount == null) return "0.00"
    val whole = amount.toLong()
    val fraction = ((amount - whole) * 100).toLong().let {
        if (it < 0) -it else it
    }
    return "$whole.${fraction.toString().padStart(2, '0')}"
}
