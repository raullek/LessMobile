package az.less.mobile.data.repository

import az.less.mobile.data.datasource.VouchersDataSource
import az.less.mobile.data.remote.model.VouchersDataDto
import az.less.mobile.domain.repository.VouchersRepository
import az.less.mobile.network.NetworkResult

class VouchersRepositoryImpl(
    private val vouchersDataSource: VouchersDataSource
) : VouchersRepository {

    override suspend fun getVouchers(
        venueId: String?,
        categoryId: String?,
        page: Int,
        limit: Int
    ): NetworkResult<VouchersDataDto> {
        return vouchersDataSource.getVouchers(venueId, categoryId, page, limit)
    }
}
