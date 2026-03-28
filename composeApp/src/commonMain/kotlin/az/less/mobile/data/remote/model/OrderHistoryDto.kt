package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderHistoryDataDto(
    val history: List<OrderHistoryGroupDto>? = null,
    val totalAmount: Double? = null,
    val totals: OrderHistoryTotalsDto? = null,
    val page: Int? = null,
    val limit: Int? = null,
    val total: Int? = null
)

@Serializable
data class OrderHistoryGroupDto(
    val date: String? = null,
    val totalAmount: Double? = null,
    val orders: List<OrderHistoryItemDto>? = null
)

@Serializable
data class OrderHistoryItemDto(
    val id: String? = null,
    val reserveNumber: String? = null,
    val client: OrderHistoryClientDto? = null,
    val box: OrderHistoryBoxDto? = null,
    val salesAmount: Double? = null,
    val commission: Double? = null,
    val amount: Double? = null,
    val completedAt: String? = null,
    val timeFormatted: String? = null
)

@Serializable
data class OrderHistoryClientDto(
    val id: String? = null,
    val name: String? = null,
    val initials: String? = null
)

@Serializable
data class OrderHistoryBoxDto(
    val id: String? = null,
    val title: String? = null
)

@Serializable
data class OrderHistoryTotalsDto(
    val sales: Double? = null,
    val commission: Double? = null,
    val paid: Double? = null
)
