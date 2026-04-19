package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Vouchers response from API
 * GET /api/v1/vouchers?venueId=&categoryId=&page=&limit=
 */
@Serializable
data class VouchersDataDto(
    val data: List<VoucherItemDto> = emptyList(),
    val pagination: VouchersPaginationDto? = null
)

@Serializable
data class VoucherItemDto(
    val id: String,
    val code: String? = null,
    val templateId: String? = null,
    val assignedAt: String? = null,
    val expiresAt: String? = null,
    val claimedAt: String? = null,
    val usedAt: String? = null,
    val usedOrderId: String? = null,
    val snapshot: VoucherSnapshotDto? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class VoucherSnapshotDto(
    val title: String? = null,
    val description: String? = null,
    val type: String? = null,
    val value: Double = 0.0,
    val currency: String? = null,
    val active: Boolean = true,
    val expiresAt: String? = null,
    val minSubtotal: Double? = null,
    val maxDiscount: Double? = null,
    val scope: VoucherScopeDto? = null
)

@Serializable
data class VoucherScopeDto(
    val venueIds: List<String> = emptyList(),
    val categoryIds: List<String> = emptyList()
)

@Serializable
data class VouchersPaginationDto(
    val page: Int = 1,
    val limit: Int = 20,
    val total: Int = 0,
    val totalPages: Int = 1,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false
)
