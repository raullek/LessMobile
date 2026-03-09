package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.DefaultPaymentDto
import az.less.mobile.data.remote.model.PaymentMethodsDto
import az.less.mobile.data.remote.model.RegisterCardDto
import az.less.mobile.domain.model.BoxDetail
import az.less.mobile.domain.model.OffersHomeData
import az.less.mobile.network.NetworkResult

interface OffersRepository {
    suspend fun getHomeOffers(
        latitude: Double?,
        longitude: Double?,
        limit: Int = 10
    ): NetworkResult<OffersHomeData>

    suspend fun getBoxDetail(boxId: String): NetworkResult<BoxDetail>

    suspend fun getDefaultPayment(): NetworkResult<DefaultPaymentDto>

    suspend fun getPaymentMethods(): NetworkResult<PaymentMethodsDto>

    suspend fun registerCard(): NetworkResult<RegisterCardDto>

    suspend fun verifyCard(callbackUrl: String): NetworkResult<Boolean>
}
