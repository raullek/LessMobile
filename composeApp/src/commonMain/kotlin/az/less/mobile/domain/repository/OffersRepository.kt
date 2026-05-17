package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.DeletePaymentMethodDto
import az.less.mobile.data.remote.model.PlaceOrderData
import az.less.mobile.data.remote.model.RegisterCardDto
import az.less.mobile.domain.model.BoxDetail
import az.less.mobile.domain.model.OffersHomeData
import az.less.mobile.domain.model.PaymentMethod
import az.less.mobile.network.NetworkResult

interface OffersRepository {
    suspend fun getHomeOffers(
        latitude: Double?,
        longitude: Double?,
        limit: Int = 10
    ): NetworkResult<OffersHomeData>

    suspend fun getBoxDetail(boxId: String): NetworkResult<BoxDetail>

    suspend fun getDefaultPayment(): NetworkResult<PaymentMethod>

    suspend fun getPaymentMethods(): NetworkResult<List<PaymentMethod>>

    suspend fun setDefaultPaymentMethod(methodId: String): NetworkResult<PaymentMethod>

    suspend fun deletePaymentMethod(methodId: String): NetworkResult<DeletePaymentMethodDto>

    suspend fun placeOrder(
        boxId: String,
        quantity: Int,
        paymentMethodId: String,
        notes: String? = null,
        userVoucherId: String? = null
    ): NetworkResult<PlaceOrderData>

    suspend fun registerCard(): NetworkResult<RegisterCardDto>

    suspend fun verifyCard(callbackUrl: String): NetworkResult<Boolean>
}
