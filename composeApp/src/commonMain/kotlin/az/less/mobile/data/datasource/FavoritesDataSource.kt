package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.FavoritesDataDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class FavoritesDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getFavorites(
        latitude: Double?,
        longitude: Double?,
        page: Int = 1,
        limit: Int = 20
    ): NetworkResult<FavoritesDataDto> {
        return safeApiCall {
            httpClient.get("v1/favorites") {
                latitude?.let { parameter("latitude", it) }
                longitude?.let { parameter("longitude", it) }
                parameter("page", page)
                parameter("limit", limit)
            }
        }
    }
}
