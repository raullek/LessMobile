package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.VouchersDataDto
import az.less.mobile.network.NetworkResult

interface VouchersRepository {
    suspend fun getVouchers(
        venueId: String? = null,
        categoryId: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): NetworkResult<VouchersDataDto>
}
