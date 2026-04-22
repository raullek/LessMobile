package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.AddFavoriteRequest
import az.less.mobile.data.remote.model.AddFavoriteResponse
import az.less.mobile.data.remote.model.CheckFavoriteResponse
import az.less.mobile.data.remote.model.FavoritesDataDto
import az.less.mobile.data.remote.model.RemoveFavoriteResponse
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

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

    suspend fun addFavorite(venueId: String): NetworkResult<AddFavoriteResponse> {
        return safeApiCall {
            httpClient.post("v1/favorites") {
                setBody(AddFavoriteRequest(venueId = venueId))
            }
        }
    }

    suspend fun removeFavorite(favoriteId: String): NetworkResult<RemoveFavoriteResponse> {
        return safeApiCall {
            httpClient.delete("v1/favorites/$favoriteId")
        }
    }

    suspend fun checkFavorite(venueId: String): NetworkResult<CheckFavoriteResponse> {
        return safeApiCall {
            httpClient.get("v1/favorites/check") {
                parameter("venueId", venueId)
            }
        }
    }
}
