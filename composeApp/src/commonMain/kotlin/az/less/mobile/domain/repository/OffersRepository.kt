package az.less.mobile.domain.repository

import az.less.mobile.domain.model.OffersHomeData
import az.less.mobile.network.NetworkResult

interface OffersRepository {
    suspend fun getHomeOffers(
        latitude: Double?,
        longitude: Double?,
        limit: Int = 10
    ): NetworkResult<OffersHomeData>
}
