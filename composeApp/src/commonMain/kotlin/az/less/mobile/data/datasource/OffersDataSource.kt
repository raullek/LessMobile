package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.BoxDetailDto
import az.less.mobile.data.remote.model.OffersScreenDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

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
}
