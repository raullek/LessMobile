package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.BoxDetailDto
import az.less.mobile.data.remote.model.DefaultPaymentDto
import az.less.mobile.data.remote.model.DeletePaymentMethodDto
import az.less.mobile.data.remote.model.OffersScreenDto
import az.less.mobile.data.remote.model.PaymentMethodsDto
import az.less.mobile.data.remote.model.PlaceOrderData
import az.less.mobile.data.remote.model.PlaceOrderRequest
import az.less.mobile.data.remote.model.RegisterCardDto
import io.ktor.client.request.setBody
import az.less.mobile.network.ApiError
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.isSuccess

class OffersDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getHomeOffers(
        latitude: Double?,
        longitude: Double?,
        limit: Int = 10
    ): NetworkResult<OffersScreenDto> {
        return safeApiCall {
            httpClient.get("v1/home/mobile") {
                latitude?.let { parameter("latitude", it) }
                longitude?.let { parameter("longitude", it) }
                parameter("limit", limit)
            }
        }
    }

    suspend fun getBoxDetail(boxId: String): NetworkResult<BoxDetailDto> {
        return safeApiCall {
            httpClient.get("v1/boxes/$boxId")
        }
    }

    suspend fun getDefaultPayment(): NetworkResult<DefaultPaymentDto> {
        return safeApiCall {
            httpClient.get("v1/payments/default")
        }
    }

    suspend fun getPaymentMethods(): NetworkResult<PaymentMethodsDto> {
        return safeApiCall {
            httpClient.get("v1/payments/methods")
        }
    }

    suspend fun registerCard(): NetworkResult<RegisterCardDto> {
        return safeApiCall {
            httpClient.post("v1/payments/cards/register")
        }
    }

    suspend fun setDefaultPaymentMethod(methodId: String): NetworkResult<DefaultPaymentDto> {
        return safeApiCall {
            httpClient.patch("v1/payments/methods/$methodId/default")
        }
    }

    suspend fun deletePaymentMethod(methodId: String): NetworkResult<DeletePaymentMethodDto> {
        return safeApiCall {
            httpClient.delete("v1/payments/methods/$methodId")
        }
    }

    suspend fun placeOrder(
        boxId: String,
        quantity: Int,
        paymentMethodId: String,
        notes: String?,
        userVoucherId: String?
    ): NetworkResult<PlaceOrderData> {
        return safeApiCall {
            httpClient.post("v1/client/orders") {
                setBody(
                    PlaceOrderRequest(
                        boxId = boxId,
                        quantity = quantity,
                        paymentMethodId = paymentMethodId,
                        notes = notes,
                        userVoucherId = userVoucherId
                    )
                )
            }
        }
    }

    suspend fun verifyCard(callbackUrl: String): NetworkResult<Boolean> {
        return try {
            val response = httpClient.get {
                url(callbackUrl)
            }
            if (response.status.isSuccess()) {
                NetworkResult.Success(data = true, message = "Card verified")
            } else {
                NetworkResult.Error(ApiError(message = "Card verification failed"))
            }
        } catch (e: Exception) {
            NetworkResult.Error(ApiError(message = e.message ?: "Network error"))
        }
    }
}
